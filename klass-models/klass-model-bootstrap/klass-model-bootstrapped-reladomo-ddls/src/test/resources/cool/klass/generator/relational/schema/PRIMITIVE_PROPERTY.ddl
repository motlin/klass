drop table if exists PRIMITIVE_PROPERTY;

create table PRIMITIVE_PROPERTY
(
    name varchar(256) not null,
    ordinal int not null,
    classifier_name varchar(256) not null,
    optional boolean not null,
    primitive_type varchar(256) not null
);

