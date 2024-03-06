drop table if exists PARAMETERIZED_PROPERTY_PARAMETER;

create table PARAMETERIZED_PROPERTY_PARAMETER
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    name varchar(256) not null,
    ordinal int not null,
    classifier_name varchar(256) not null,
    parameterized_property_name varchar(256) not null
);

