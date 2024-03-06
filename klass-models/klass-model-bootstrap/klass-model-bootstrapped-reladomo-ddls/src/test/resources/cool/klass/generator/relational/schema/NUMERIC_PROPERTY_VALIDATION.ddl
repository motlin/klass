drop table if exists NUMERIC_PROPERTY_VALIDATION;

create table NUMERIC_PROPERTY_VALIDATION
(
    classifier_name varchar(256) not null,
    property_name varchar(256) not null,
    number int not null
);

