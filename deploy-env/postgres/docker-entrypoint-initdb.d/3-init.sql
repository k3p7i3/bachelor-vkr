\c postgres;

create user "agent-service-liquibase" with password 'agent-service-liquibase';
create user "agent-service" with password 'agent-service';

create database agent_database with owner = 'postgres';

\c agent_database;

create schema ext;

grant usage on schema ext to "agent-service-liquibase";
grant usage on schema ext to "agent-service";

alter role "agent-service-liquibase" set search_path = agent_database,ext,public;
alter role "agent-service" set search_path = agent_database,ext,public;

create extension "uuid-ossp" schema ext;

create schema agent_database authorization "agent-service-liquibase";

grant usage on schema agent_database to "agent-service";

alter default privileges for user "agent-service-liquibase" in schema "agent_database" grant select, update, insert, delete on tables to "agent-service";
alter default privileges for user "agent-service-liquibase" in schema "agent_database" grant usage, select, update on sequences to "agent-service";
