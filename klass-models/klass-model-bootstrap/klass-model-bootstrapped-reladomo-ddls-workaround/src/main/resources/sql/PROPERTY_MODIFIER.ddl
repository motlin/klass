drop table if exists PROPERTY_MODIFIER;

create table PROPERTY_MODIFIER
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    name varchar(255) not null,
    ordinal int not null,
    classifier_name varchar(255) not null,
    property_name varchar(255) not null
);

