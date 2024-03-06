drop table if exists PARAMETERIZED_PROPERTY_PARAMETER;

create table PARAMETERIZED_PROPERTY_PARAMETER
(
    name varchar(256) not null,
    ordinal int not null,
    classifier_name varchar(256) not null,
    parameterized_property_name varchar(256) not null
);

