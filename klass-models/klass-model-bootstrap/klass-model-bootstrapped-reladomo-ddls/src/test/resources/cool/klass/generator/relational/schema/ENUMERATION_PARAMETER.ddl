drop table if exists ENUMERATION_PARAMETER;

create table ENUMERATION_PARAMETER
(
    name varchar(256) not null,
    id bigint not null,
    enumeration_name varchar(256) not null,
    ordinal int not null,
    multiplicity varchar(256) not null
);

