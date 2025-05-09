--liquibase formatted sql
--changeset pkopyrina:id-1 logicalFilePath:01.000.00/review.sql
create table review
(
    id                      uuid             not null default uuid_generate_v4()
        constraint pk_review primary key,
    author_id               uuid             not null,
    agent_id                uuid             not null
        references agent_profile(id),
    review_date             timestamp        not null,
    grade                   integer          not null,
    text                    varchar          null
);
--rollback drop table review;