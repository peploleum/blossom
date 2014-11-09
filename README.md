#blossom

A sandbox Eclipse project involving some Java EE components (JPA, JAX-RS, Servlets) and a Javascript heavy front end based on AngularJS, d3js, OpenLayers 3.0.

## Environment

* tomcat 8.0.2

* solr 4.9.0

* postgreSQL 9.3 with postGIS

* eclipse jee luna 4.4.0

* maven 3.2.3

* chrome 38.something

## Howto deploy (same for linux and windows tested @ Windows Server 2008 R2 & Ubuntu 14.something)

Install Maven (with Tomcat plugin) ,Tomcat, PostgreSQL/postGIS.

Run Blossom/src/blossom/persistence/bootstrap.sql in a postGIS enabled postGRES database.

Edit persistence.xml to match local db options.

Edit pom.xml to match local Tomcat options then run target 'mvn clean compile install tomcat7:redeploy'.


## Howto dev

Later.

