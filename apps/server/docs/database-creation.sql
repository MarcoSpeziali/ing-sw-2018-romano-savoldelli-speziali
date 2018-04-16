CREATE TABLE IF NOT EXISTS player (
  id SERIAL PRIMARY KEY, -- int auto_increment
  username VARCHAR(32) UNIQUE NOT NULL,
  password CHAR(32) NOT NULL, -- md5
  played_games INT DEFAULT 0 NOT NULL,
  total_points INT DEFAULT 0 NOT NULL,
  wins_count INT DEFAULT 0 NOT NULL
);

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE pg_type.typname = 'protocol') THEN
      CREATE TYPE PROTOCOL AS ENUM (
        'socket',
        'rmi'
      );
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS session (
  id SERIAL PRIMARY KEY,
  player INTEGER REFERENCES player (id) NOT NULL,
  creation_time TIMESTAMP DEFAULT current_timestamp NOT NULL,
  expiration_time TIMESTAMP,
  token CHAR(32) NOT NULL,
  public_key CHAR(266) UNIQUE NOT NULL,
  protocol PROTOCOL NOT NULL,
  previous_session INTEGER REFERENCES session (id),
  ip CIDR NOT NULL
);

CREATE TABLE IF NOT EXISTS lobby (
  id SERIAL PRIMARY KEY,
  opening_time TIMESTAMP DEFAULT current_timestamp NOT NULL,
  closing_time TIMESTAMP,
  owner INTEGER REFERENCES player (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS private_lobby  (
  access_token CHAR(8) NOT NULL
) INHERITS (lobby);

CREATE TABLE IF NOT EXISTS player_lobby (
  player INTEGER REFERENCES player (id),
  lobby INTEGER REFERENCES lobby (id),
  PRIMARY KEY (player, lobby)
);

CREATE TABLE IF NOT EXISTS match (
  id SERIAL PRIMARY KEY,
  starting_time TIMESTAMP DEFAULT current_timestamp NOT NULL,
  ending_time TIMESTAMP,
  lobby INTEGER REFERENCES lobby (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS result (
  id SERIAL PRIMARY KEY,
  player INTEGER REFERENCES player (id) NOT NULL,
  match INTEGER REFERENCES match (id) NOT NULL,
  points INTEGER NOT NULL
);

-- FUNCTIONS --
CREATE OR REPLACE FUNCTION update_statistics()
  RETURNS TRIGGER AS $$
BEGIN
  UPDATE player
  SET
    player.played_games = player.played_games + 1,
    player.total_points = player.total_points + NEW.points
  WHERE player.id = NEW.player;
END;
$$
LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION update_win_count()
  RETURNS TRIGGER AS $$
BEGIN
  UPDATE player
  SET
    player.wins_count = player.wins_count + ((
      SELECT r.player
      FROM result r
      WHERE r.match = NEW.id -- TODO: CHANGE ME
      GROUP BY r.match
      HAVING MAX(points)
    ) = player.id)::INT
  WHERE player.id IN (
    SELECT pr.player
    FROM match m2
    JOIN lobby r ON m2.lobby = r.id
    JOIN player_lobby pr ON r.id = pr.lobby
    WHERE m2.id = NEW.id
  );
END;
$$
LANGUAGE 'plpgsql';

-- TRIGGERS --

CREATE TRIGGER on_new_result
  AFTER INSERT
  ON result
EXECUTE PROCEDURE update_statistics();

CREATE TRIGGER on_closed_match
  AFTER UPDATE OF ending_time
  ON match
EXECUTE PROCEDURE update_win_count();