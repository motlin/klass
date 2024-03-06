drop table if exists ENUMERATION_PROPERTY;

create table ENUMERATION_PROPERTY
(
    classifier_name varchar(256) not null,
    name varchar(256) not null,
    enumeration_name varchar(256) not null,
    ordinal int not null,
    optional boolean not null
);

