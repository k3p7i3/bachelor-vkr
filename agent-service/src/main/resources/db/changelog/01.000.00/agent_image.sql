--liquibase formatted sql
--changeset pkopyrina:id-1 logicalFilePath:01.000.00/agent_image.sql
create table agent_image
(
    id                      uuid             not null default uuid_generate_v4()
        constraint pk_agent_image primary key,
    agent_id                uuid             not null
        references agent_profile(id),
    s3_file_path            varchar(512)     not null,
    created_at              timestamp        not null,
    avatar                  boolean          not null
);
--rollback drop table agent_image;