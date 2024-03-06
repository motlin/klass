drop table if exists PROPERTY_MODIFIER;

create table PROPERTY_MODIFIER
(
    name varchar(256) not null,
    ordinal int not null,
    classifier_name varchar(256) not null,
    property_name varchar(256) not null
);

