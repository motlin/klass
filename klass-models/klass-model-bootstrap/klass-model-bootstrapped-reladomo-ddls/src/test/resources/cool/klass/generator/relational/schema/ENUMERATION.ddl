drop table if exists ENUMERATION;

create table ENUMERATION
(
    name varchar(256) not null,
    ordinal int not null,
    package_name varchar(100000) not null
);

