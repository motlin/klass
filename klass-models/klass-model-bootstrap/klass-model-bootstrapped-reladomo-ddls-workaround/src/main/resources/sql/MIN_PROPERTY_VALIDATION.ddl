drop table if exists MIN_PROPERTY_VALIDATION;

create table MIN_PROPERTY_VALIDATION
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    classifier_name varchar(255) not null,
    property_name varchar(255) not null,
    number int not null
);

