drop table if exists DATA_TYPE_PROPERTY;

create table DATA_TYPE_PROPERTY
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    name varchar(256) not null,
    ordinal int not null,
    classifier_name varchar(256) not null,
    optional boolean not null
);

