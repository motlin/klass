drop table if exists ENUMERATION_PROPERTY;

create table ENUMERATION_PROPERTY
(
    name varchar(256) not null,
    ordinal int not null,
    classifier_name varchar(256) not null,
    optional boolean not null,
    enumeration_name varchar(256) not null
);

