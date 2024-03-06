drop table if exists CLASSIFIER;

create table CLASSIFIER
(
    name varchar(256) not null,
    ordinal int not null,
    package_name varchar(100000) not null
);

