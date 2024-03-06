drop table if exists PRIMITIVE_PROPERTY;

create table PRIMITIVE_PROPERTY
(
    classifier_name varchar(255) not null,
    name varchar(255) not null,
    primitive_type varchar(255) not null,
    id boolean not null
);
