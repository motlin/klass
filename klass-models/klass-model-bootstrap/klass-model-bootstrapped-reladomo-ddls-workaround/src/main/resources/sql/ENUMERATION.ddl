drop table if exists ENUMERATION;

create table ENUMERATION
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    name varchar(256) not null,
    ordinal int not null,
    package_name varchar(100000) not null
);

