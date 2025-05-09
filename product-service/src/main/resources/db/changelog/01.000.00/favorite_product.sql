--liquibase formatted sql
--changeset pkopyrina:id-1 logicalFilePath:01.000.00/favorite_product.sql
create table favorite_product
(
    id                      uuid        not null default uuid_generate_v4()
        constraint pk_favorite_product primary key,
    product_id              uuid        not null
        references product(id),
    user_id                 uuid        not null,
    created_at              timestamp   not null default now()
);
--rollback drop table favorite_product;