-- Schema: blossom the very complex bootstrap script ...

DROP SCHEMA blossom CASCADE;

CREATE SCHEMA blossom
  AUTHORIZATION postgres;
  
  -- Table: blossom."character"
DROP TABLE blossom."character";

CREATE TABLE blossom."character"
(
   id varchar(36) primary key not null,
   name varchar(250),
   catchphrase varchar(250),
   size integer
)
WITH (
OIDS=FALSE
);
ALTER TABLE blossom."character"
OWNER TO postgres;

  -- Table: blossom."organization"
DROP TABLE blossom."organisation";

CREATE TABLE blossom."organisation"
(
   id varchar(36) primary key not null,
   name varchar(250)
)
WITH (
OIDS=FALSE
);
ALTER TABLE blossom."organisation"
OWNER TO postgres;

  -- Table: blossom."place"
DROP TABLE blossom."place";

CREATE TABLE blossom."place"
(
   id varchar(36) primary key not null,
   name varchar(250)
)
WITH (
OIDS=FALSE
);
ALTER TABLE blossom."place"
OWNER TO postgres;

  -- Table: blossom."event"
DROP TABLE blossom."event";

CREATE TABLE blossom."event"
(
   id varchar(36) primary key not null,
   name varchar(250)
)
WITH (
OIDS=FALSE
);
ALTER TABLE blossom."event"
OWNER TO postgres;

  -- Table: blossom."equipment"
DROP TABLE blossom."equipment";

CREATE TABLE blossom."equipment"
(
   id varchar(36) primary key not null,
   name varchar(250)
)
WITH (
OIDS=FALSE
);


ALTER TABLE blossom."equipment"
OWNER TO postgres;



DROP TABLE blossom."link";
CREATE TABLE blossom."link"
(
   source varchar(36),
   dest varchar(36),
   name varchar(250)
);
ALTER TABLE blossom."link"
OWNER TO postgres;

DROP TABLE blossom."location";
CREATE TABLE blossom."location" ( 
  id varchar(36) PRIMARY KEY,
  geom GEOMETRY(Point, 4326)
); 
 ALTER TABLE blossom."location"
OWNER TO postgres;

DROP INDEX location_gix;
CREATE INDEX location_gix
  ON blossom."location" 
  USING GIST (geom); 
  
