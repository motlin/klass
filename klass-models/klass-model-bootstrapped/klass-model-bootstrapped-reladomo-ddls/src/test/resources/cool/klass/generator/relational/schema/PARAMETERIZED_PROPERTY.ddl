drop table if exists PARAMETERIZED_PROPERTY;

create table PARAMETERIZED_PROPERTY
(
    owning_class_name varchar(256) not null,
    name varchar(256) not null,
    result_type_name varchar(256) not null,
    ordinal int not null,
    multiplicity varchar(256) not null
);

