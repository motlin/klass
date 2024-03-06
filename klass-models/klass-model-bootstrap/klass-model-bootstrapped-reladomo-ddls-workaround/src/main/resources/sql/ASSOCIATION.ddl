drop table if exists ASSOCIATION;

create table ASSOCIATION
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    name varchar(255) not null,
    ordinal int not null,
    package_name varchar(255) not null,
    criteria_id bigint not null
);

