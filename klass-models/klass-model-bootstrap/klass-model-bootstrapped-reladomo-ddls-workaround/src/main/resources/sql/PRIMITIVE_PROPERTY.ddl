drop table if exists PRIMITIVE_PROPERTY;

create table PRIMITIVE_PROPERTY
(
    classifier_name varchar(256) not null,
    name varchar(256) not null,
    primitive_type varchar(256) not null
);
