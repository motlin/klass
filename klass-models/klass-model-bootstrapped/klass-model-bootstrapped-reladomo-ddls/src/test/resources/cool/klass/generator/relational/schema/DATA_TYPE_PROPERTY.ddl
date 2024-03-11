drop table if exists DATA_TYPE_PROPERTY;

create table DATA_TYPE_PROPERTY
(
    classifier_name varchar(256) not null,
    name varchar(256) not null,
    ordinal int not null,
    optional boolean not null
);

