drop table if exists ENUMERATION_LITERAL;

create table ENUMERATION_LITERAL
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    name varchar(255) not null,
    ordinal int not null,
    enumeration_name varchar(255) not null,
    pretty_name varchar(255)
);

