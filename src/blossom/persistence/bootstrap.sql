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

CREATE TABLE blossom."link"
(
   source varchar(36) primary key not null,
   dest varchar(36),
   name varchar(250)
);
ALTER TABLE blossom."link"
OWNER TO postgres;
