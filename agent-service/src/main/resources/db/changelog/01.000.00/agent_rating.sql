--liquibase formatted sql
--changeset pkopyrina:id-1 logicalFilePath:01.000.00/agent_rating.sql
create table agent_rating
(
    id                      uuid             not null default uuid_generate_v4()
        constraint pk_agent_rating primary key,
    agent_id                uuid             not null
        references agent_profile(id),
    grades_sum              integer          not null,
    reviews_number          integer          not null,
    average_grade           decimal(25, 10)  not null,
    recommendation_score    decimal(25, 10)  not null
);
--rollback drop table agent_rating;

--changeset pkopyrina:id-2 logicalFilePath:01.000.00/agent_rating.sql
create index if not exists agent_rating_recommendation_ix on agent_rating(recommendation_score);
--rollback drop index if exists agent_rating_recommendation_ix;