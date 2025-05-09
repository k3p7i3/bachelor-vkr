\c postgres;

create user "order-service-liquibase" with password 'order-service-liquibase';
create user "order-service" with password 'order-service';

create database order_database with owner = 'postgres';

\c order_database;

create schema ext;

grant usage on schema ext to "order-service-liquibase";
grant usage on schema ext to "order-service";

alter role "order-service-liquibase" set search_path = order_database,ext,public;
alter role "order-service" set search_path = order_database,ext,public;

create extension "uuid-ossp" schema ext;

create schema order_database authorization "order-service-liquibase";

grant usage on schema order_database to "order-service";

alter default privileges for user "order-service-liquibase" in schema "order_database" grant select, update, insert, delete on tables to "order-service";
alter default privileges for user "order-service-liquibase" in schema "order_database" grant usage, select, update on sequences to "order-service";
