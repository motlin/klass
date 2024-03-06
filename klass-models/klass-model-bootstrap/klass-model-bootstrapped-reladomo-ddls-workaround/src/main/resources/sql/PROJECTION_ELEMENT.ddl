drop table if exists PROJECTION_ELEMENT;

create table PROJECTION_ELEMENT
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    ordinal int not null,
    name varchar(255) not null,
    id bigint not null,
    parent_id bigint
);

