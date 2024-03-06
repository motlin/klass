drop table if exists PARAMETER;

create table PARAMETER
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    ordinal int not null,
    name varchar(256) not null,
    id bigint not null,
    multiplicity varchar(256) not null
);

