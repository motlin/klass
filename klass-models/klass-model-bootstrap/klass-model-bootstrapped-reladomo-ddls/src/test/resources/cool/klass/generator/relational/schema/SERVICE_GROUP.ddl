drop table if exists SERVICE_GROUP;

create table SERVICE_GROUP
(
    class_name varchar(256) not null,
    name varchar(256) not null,
    ordinal int not null,
    package_name varchar(100000) not null
);

