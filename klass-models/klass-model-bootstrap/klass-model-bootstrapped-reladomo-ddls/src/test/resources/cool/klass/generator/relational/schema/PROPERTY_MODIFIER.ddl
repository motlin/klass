drop table if exists PROPERTY_MODIFIER;

create table PROPERTY_MODIFIER
(
    classifier_name varchar(256) not null,
    property_name varchar(256) not null,
    keyword varchar(256) not null,
    ordinal int not null
);

