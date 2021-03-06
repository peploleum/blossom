DROP SCHEMA blossom CASCADE;

CREATE SCHEMA blossom
  AUTHORIZATION postgres;
  
DROP TABLE blossom."entity";

CREATE TABLE blossom."entity"
(
   id varchar(36) primary key not null,
   name varchar(250),
   id_symbol varchar(36)
)
WITH (
OIDS=FALSE
);
ALTER TABLE blossom."entity"
OWNER TO postgres;  
  
DROP TABLE blossom."character";

CREATE TABLE blossom."character"
(
   id varchar(36) primary key not null,
   catchphrase varchar(250),
   firstname varchar(250),
   lastname varchar(250)
)
WITH (
OIDS=FALSE
);
ALTER TABLE blossom."character"
OWNER TO postgres;

DROP TABLE blossom."organisation";

CREATE TABLE blossom."organisation"
(
   id varchar(36) primary key not null
)
WITH (
OIDS=FALSE
);
ALTER TABLE blossom."organisation"
OWNER TO postgres;

DROP TABLE blossom."place";

CREATE TABLE blossom."place"
(
   id varchar(36) primary key not null
)
WITH (
OIDS=FALSE
);
ALTER TABLE blossom."place"
OWNER TO postgres;

DROP TABLE blossom."event";

CREATE TABLE blossom."event"
(
   id varchar(36) primary key not null
)
WITH (
OIDS=FALSE
);
ALTER TABLE blossom."event"
OWNER TO postgres;

DROP TABLE blossom."equipment";

CREATE TABLE blossom."equipment"
(
   id varchar(36) primary key not null
)
WITH (
OIDS=FALSE
);


ALTER TABLE blossom."equipment"
OWNER TO postgres;

DROP TABLE blossom."tech_symbol";

CREATE TABLE blossom."tech_symbol"
(
   id varchar(36) primary key not null,
   size int,
   content bytea
)
WITH (
OIDS=FALSE
);

ALTER TABLE blossom."tech_symbol"
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
  
