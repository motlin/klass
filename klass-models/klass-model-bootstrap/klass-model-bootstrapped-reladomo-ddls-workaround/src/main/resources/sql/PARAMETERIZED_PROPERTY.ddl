drop table if exists PARAMETERIZED_PROPERTY;

create table PARAMETERIZED_PROPERTY
(
    name varchar(256) not null,
    ordinal int not null,
    owning_class_name varchar(256) not null,
    multiplicity varchar(256) not null,
    result_type_name varchar(256) not null
);

