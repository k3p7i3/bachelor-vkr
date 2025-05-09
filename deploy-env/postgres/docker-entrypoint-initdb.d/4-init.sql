\c postgres;

create user "tariff-service-liquibase" with password 'tariff-service-liquibase';
create user "tariff-service" with password 'tariff-service';

create database tariff_database with owner = 'postgres';

\c tariff_database;

create schema ext;

grant usage on schema ext to "tariff-service-liquibase";
grant usage on schema ext to "tariff-service";

alter role "tariff-service-liquibase" set search_path = tariff_database,ext,public;
alter role "tariff-service" set search_path = tariff_database,ext,public;

create extension "uuid-ossp" schema ext;

create schema tariff_database authorization "tariff-service-liquibase";

grant usage on schema tariff_database to "tariff-service";

alter default privileges for user "tariff-service-liquibase" in schema "tariff_database" grant select, update, insert, delete on tables to "tariff-service";
alter default privileges for user "tariff-service-liquibase" in schema "tariff_database" grant usage, select, update on sequences to "tariff-service";
