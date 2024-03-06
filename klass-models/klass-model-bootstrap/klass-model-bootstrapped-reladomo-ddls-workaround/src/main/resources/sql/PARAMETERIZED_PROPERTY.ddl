drop table if exists PARAMETERIZED_PROPERTY;

create table PARAMETERIZED_PROPERTY
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    name varchar(256) not null,
    ordinal int not null,
    owning_class_name varchar(256) not null,
    multiplicity varchar(256) not null,
    result_type_name varchar(256) not null
);

