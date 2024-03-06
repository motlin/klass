drop table if exists MAX_LENGTH_PROPERTY_VALIDATION;

create table MAX_LENGTH_PROPERTY_VALIDATION
(
    classifier_name varchar(256) not null,
    property_name varchar(256) not null,
    number int not null
);

