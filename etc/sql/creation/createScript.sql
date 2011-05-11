-- Create Role, Database, e.t.c


-- DROP ROLE teamsched_group;

CREATE ROLE teamsched_group
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;

-- Role: "teamsched_user"

-- DROP ROLE teamsched_user;

CREATE ROLE teamsched_user LOGIN
  ENCRYPTED PASSWORD 'md55bd42d19689e52001b5d7d3f042d52c2'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;
GRANT teamsched_group TO teamsched_user;

CREATE DATABASE teamsched
  WITH ENCODING='UTF8';

CREATE SCHEMA ts6
  AUTHORIZATION postgres;
GRANT ALL ON SCHEMA ts6 TO postgres;
GRANT USAGE on SCHEMA ts6 to teamsched_group;

-- we need language for store procedures
CREATE LANGUAGE plpgsql;


