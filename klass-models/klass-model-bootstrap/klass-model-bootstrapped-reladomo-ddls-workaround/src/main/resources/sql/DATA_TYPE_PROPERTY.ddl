drop table if exists DATA_TYPE_PROPERTY;

create table DATA_TYPE_PROPERTY
(
    name varchar(256) not null,
    ordinal int not null,
    classifier_name varchar(256) not null,
    optional boolean not null
);

