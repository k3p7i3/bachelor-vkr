--liquibase formatted sql
--changeset pkopyrina:id-1 logicalFilePath:01.000.00/user.sql
create table users
(
    id                      uuid             not null default uuid_generate_v4()
        constraint pk_user primary key,
    first_name              varchar(128)     not null,
    last_name               varchar(128)     not null,
    email                   varchar(256)     not null
        constraint uq_user_email unique,
    password                varchar(1024)    null,
    role                    varchar(64)      not null,
    agent_id                uuid             null
);
--rollback drop table users;