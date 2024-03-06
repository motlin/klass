drop table if exists ENUMERATION_LITERAL;

create table ENUMERATION_LITERAL
(
    enumeration_name varchar(256) not null,
    name varchar(256) not null,
    ordinal int not null,
    pretty_name varchar(256)
);

