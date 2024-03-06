drop table if exists ENUMERATION_PROPERTY;

create table ENUMERATION_PROPERTY
(
    classifier_name varchar(255) not null,
    name varchar(255) not null,
    enumeration_name varchar(255) not null,
);
