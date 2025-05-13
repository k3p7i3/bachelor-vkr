--liquibase formatted sql
--changeset pkopyrina:id-1 logicalFilePath:01.000.00/agent_profile.sql
create table agent_profile
(
    id                      uuid             not null default uuid_generate_v4()
        constraint pk_agent_profile primary key,
    name                    varchar(256)     not null,
    description             varchar          null,
    phone_number            varchar(64)      null,
    email                   varchar(128)     null,
    legal_name               varchar(1024)    null
);
--rollback drop table agent_profile;