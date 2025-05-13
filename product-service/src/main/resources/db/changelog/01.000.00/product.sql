--liquibase formatted sql
--changeset pkopyrina:id-1 logicalFilePath:01.000.00/product.sql
create table product
(
    id                      uuid        not null default uuid_generate_v4()
        constraint pk_product primary key,
    is_custom               boolean     not null,
    external_id             varchar     null,
    sku_id                  varchar     null
        constraint uq_product_sku_id unique,
    sku_parameters          varchar[]   null,
    marketplace             varchar(32) not null,
    product_url             varchar     not null,
    image_url               varchar     null,
    title                   varchar     null,
    weight                  jsonb       null,
    box_volume              jsonb       null,
    volume                  jsonb       null,
    price                   jsonb       null
);
--rollback drop table product;