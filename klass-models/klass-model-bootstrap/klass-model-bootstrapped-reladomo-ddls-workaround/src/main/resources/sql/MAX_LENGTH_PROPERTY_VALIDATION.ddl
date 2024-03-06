drop table if exists MAX_LENGTH_PROPERTY_VALIDATION;

create table MAX_LENGTH_PROPERTY_VALIDATION
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    classifier_name varchar(256) not null,
    property_name varchar(256) not null,
    number int not null
);

