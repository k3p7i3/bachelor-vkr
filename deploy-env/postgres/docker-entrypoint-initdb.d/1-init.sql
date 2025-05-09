\c postgres;

create user "user-service-liquibase" with password 'user-service-liquibase';
create user "user-service" with password 'user-service';

create database user_database with owner = 'postgres';

\c user_database;

create schema ext;

grant usage on schema ext to "user-service-liquibase";
grant usage on schema ext to "user-service";

alter role "user-service-liquibase" set search_path = user_database,ext,public;
alter role "user-service" set search_path = user_database,ext,public;

create extension "uuid-ossp" schema ext;

create schema user_database authorization "user-service-liquibase";

grant usage on schema user_database to "user-service";

alter default privileges for user "user-service-liquibase" in schema "user_database" grant select, update, insert, delete on tables to "user-service";
alter default privileges for user "user-service-liquibase" in schema "user_database" grant usage, select, update on sequences to "user-service";
