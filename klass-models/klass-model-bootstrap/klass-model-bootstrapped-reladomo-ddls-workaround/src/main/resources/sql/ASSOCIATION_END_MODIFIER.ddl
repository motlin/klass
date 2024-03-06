drop table if exists ASSOCIATION_END_MODIFIER;

create table ASSOCIATION_END_MODIFIER
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    name varchar(255) not null,
    ordinal int not null,
    owning_class_name varchar(255) not null,
    association_end_name varchar(255) not null
);

