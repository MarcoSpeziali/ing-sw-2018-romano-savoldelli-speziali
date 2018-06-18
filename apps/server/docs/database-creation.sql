-- PERFORM dblink_exec('dbname=' || current_database(), 'CREATE DATABASE sagrada');
-- CREATE USER sagrada WITH ENCRYPTED PASSWORD 'jjn6sjI2F34~cicv=aHB]vjqLVw3-CgSbEgFSq}@QMhuuL)DF)zzE$Y5X&FFHGYs';
-- GRANT ALL PRIVILEGES ON DATABASE sagrada TO sagrada;

CREATE TABLE IF NOT EXISTS player (
  id SERIAL PRIMARY KEY,
  username VARCHAR(32) UNIQUE NOT NULL,
  password CHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS pre_authentication_session (
  id SERIAL PRIMARY KEY,
  player INTEGER NOT NULL REFERENCES player(id) ON UPDATE CASCADE ON DELETE CASCADE,
  expected_challenge_response CHAR(40) NOT NULL,
  creation_time TIMESTAMP(3) NOT NULL DEFAULT current_timestamp,
  invalidation_time TIMESTAMP(3) NOT NULL DEFAULT current_timestamp + INTERVAL '2 minutes',
  valid_for_ip CIDR NOT NULL,
  valid_for_port INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS session (
  id SERIAL PRIMARY KEY,
  creation_time TIMESTAMP(3) NOT NULL DEFAULT current_timestamp,
  invalidation_time TIMESTAMP(3) NOT NULL DEFAULT current_timestamp + INTERVAL '12 hours',
  token CHAR(40) NOT NULL,
  pre_auth_session INTEGER NOT NULL REFERENCES pre_authentication_session(id)
);

CREATE TABLE IF NOT EXISTS lobby (
  id SERIAL PRIMARY KEY,
  opening_time TIMESTAMP(3) NOT NULL DEFAULT current_timestamp,
  closing_time TIMESTAMP(3) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS lobby_player (
  player INTEGER NOT NULL REFERENCES player(id) ON UPDATE CASCADE ON DELETE CASCADE,
  lobby INTEGER NOT NULL REFERENCES lobby(id) ON UPDATE CASCADE ON DELETE CASCADE,
  joining_time TIMESTAMP(3) NOT NULL DEFAULT current_timestamp,
  leaving_time TIMESTAMP(3) DEFAULT NULL,
  PRIMARY KEY (lobby, player, joining_time)
);

CREATE TABLE IF NOT EXISTS match (
  id SERIAL PRIMARY KEY,
  starting_time TIMESTAMP(3) NOT NULL DEFAULT current_timestamp,
  ending_time TIMESTAMP(3) DEFAULT NULL,
  from_lobby INTEGER NOT NULL REFERENCES lobby(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS match_player (
  player INTEGER NOT NULL REFERENCES player(id) ON UPDATE CASCADE ON DELETE CASCADE,
  "match" INTEGER NOT NULL REFERENCES match(id) ON UPDATE CASCADE ON DELETE CASCADE,
  joining_time TIMESTAMP(3) NOT NULL DEFAULT current_timestamp,
  leaving_time TIMESTAMP(3) DEFAULT NULL,
  PRIMARY KEY (player, "match")
);

CREATE TABLE IF NOT EXISTS result (
  player INTEGER NOT NULL REFERENCES player(id) ON UPDATE CASCADE ON DELETE CASCADE,
  "match" INTEGER NOT NULL REFERENCES match(id) ON UPDATE CASCADE ON DELETE CASCADE,
  points INTEGER NOT NULL,
  PRIMARY KEY (player, "match")
);