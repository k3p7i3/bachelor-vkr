--liquibase formatted sql
--changeset pkopyrina:id-1 logicalFilePath:01.000.00/agent_selection.sql
create table agent_selection
(
    id                      uuid             not null default uuid_generate_v4()
        constraint pk_agent_selection primary key,
    user_id                 uuid             not null
        constraint uq_agent_selection_user_id unique,
    agent_id                uuid             not null
        references agent_profile(id)
);
--rollback drop table agent_selection;