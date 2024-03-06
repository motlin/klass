drop table if exists ENUMERATION_LITERAL;

create table ENUMERATION_LITERAL
(
    name varchar(256) not null,
    ordinal int not null,
    enumeration_name varchar(256) not null,
    pretty_name varchar(256)
);

