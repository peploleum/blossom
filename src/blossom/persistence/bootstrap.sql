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
  
