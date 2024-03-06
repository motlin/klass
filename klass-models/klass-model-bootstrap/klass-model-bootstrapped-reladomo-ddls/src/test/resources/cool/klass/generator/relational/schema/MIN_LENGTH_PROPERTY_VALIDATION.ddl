drop table if exists MIN_LENGTH_PROPERTY_VALIDATION;

create table MIN_LENGTH_PROPERTY_VALIDATION
(
    classifier_name varchar(256) not null,
    property_name varchar(256) not null,
    number int not null
);

