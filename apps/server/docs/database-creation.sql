CREATE TABLE IF NOT EXISTS player (
  id SERIAL PRIMARY KEY,
  username VARCHAR(32) UNIQUE NOT NULL,
  password CHAR(32) NOT NULL,
  played_games INT DEFAULT 0 NOT NULL,
  total_points INT DEFAULT 0 NOT NULL,
  wins_count INT DEFAULT 0 NOT NULL
);

CREATE TYPE PROTOCOL AS ENUM (
  'socket',
  'rmi'
);

CREATE TABLE IF NOT EXISTS session (
  id SERIAL PRIMARY KEY,
  player INTEGER REFERENCES player (id) NOT NULL,
  creation_time TIMESTAMP DEFAULT current_timestamp NOT NULL,
  invalidation_time TIMESTAMP,
  token CHAR(32),
  public_key CHAR(266) UNIQUE NOT NULL,
  protocol PROTOCOL NOT NULL,
  previous_session INTEGER REFERENCES session (id),
  ip CIDR NOT NULL
);

CREATE TABLE IF NOT EXISTS room (
  id SERIAL PRIMARY KEY,
  opening_time TIMESTAMP DEFAULT current_timestamp NOT NULL,
  closing_time TIMESTAMP,
  owner INTEGER REFERENCES player (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS private_room  (
  access_token CHAR(8) NOT NULL
) INHERITS (room);

CREATE TABLE IF NOT EXISTS player_room (
  player INTEGER REFERENCES player (id),
  room INTEGER REFERENCES room (id),
  PRIMARY KEY (player, room)
);

CREATE TABLE IF NOT EXISTS match (
  id SERIAL PRIMARY KEY,
  starting_time TIMESTAMP DEFAULT current_timestamp NOT NULL,
  ending_time TIMESTAMP,
  room INTEGER REFERENCES room (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS result (
  id SERIAL PRIMARY KEY,
  player INTEGER REFERENCES player (id) NOT NULL,
  match INTEGER REFERENCES match (id) NOT NULL,
  points INTEGER NOT NULL
);

-- FUNCTIONS --
CREATE OR REPLACE FUNCTION update_statistics(res result)
  AS $$
BEGIN
  UPDATE player
  SET
    player.played_games = player.played_games + 1,
    player.total_points = player.total_points + res.points
  WHERE player.id = res.player;
END;
$$
LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION update_win_count(m match)
  AS $$
BEGIN
  UPDATE player
  SET
    player.wins_count = player.wins_count + ((
      SELECT r.player
      FROM result r
      WHERE r.match = m.id
      GROUP BY r.match
      HAVING MAX(points)
    ) = player.id)::INT
  WHERE player.id IN (
    SELECT pr.player
    FROM match m2
    JOIN room r ON m2.room = r.id
    JOIN player_room pr ON r.id = pr.room
    WHERE m2.id = m.id
  );
END;
$$
LANGUAGE 'plpgsql';

-- TRIGGERS --

CREATE TRIGGER on_new_result
  AFTER INSERT
  ON result
EXECUTE PROCEDURE update_statistics(NEW.player, NEW.points);

CREATE TRIGGER on_closed_match
  AFTER UPDATE OF ending_time
  ON match
  WHEN (OLD.ending_time is NULL and NEW.ending_time is not NULL)
EXECUTE PROCEDURE update_win_count(NEW);