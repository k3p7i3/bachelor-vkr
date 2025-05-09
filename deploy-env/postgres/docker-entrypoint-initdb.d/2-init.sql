\c postgres;

create user "product-service-liquibase" with password 'product-service-liquibase';
create user "product-service" with password 'product-service';

create database product_database with owner = 'postgres';

\c product_database;

create schema ext;

grant usage on schema ext to "product-service-liquibase";
grant usage on schema ext to "product-service";

alter role "product-service-liquibase" set search_path = product_database,ext,public;
alter role "product-service" set search_path = product_database,ext,public;

create extension "uuid-ossp" schema ext;

create schema product_database authorization "product-service-liquibase";

grant usage on schema product_database to "product-service";

alter default privileges for user "product-service-liquibase" in schema "product_database" grant select, update, insert, delete on tables to "product-service";
alter default privileges for user "product-service-liquibase" in schema "product_database" grant usage, select, update on sequences to "product-service";
