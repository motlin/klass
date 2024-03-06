drop table if exists PACKAGEABLE_ELEMENT;

create table PACKAGEABLE_ELEMENT
(
    name varchar(256) not null,
    ordinal int not null,
    package_name varchar(100000) not null
);

