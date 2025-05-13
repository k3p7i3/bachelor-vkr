--liquibase formatted sql
--changeset pkopyrina:id-1 logicalFilePath:01.000.00/oauth2_client.sql
create table oauth2_client
(
    id                      uuid        not null default uuid_generate_v4()
        constraint pk_oauth2_client primary key,
    client_id               varchar     null,
    client_secret           varchar     null,
    redirect_uri            varchar     null,
    scope                   varchar     null,
    auth_method             varchar     null,
    grant_type              varchar     null
);
--rollback drop table oauth2_client;