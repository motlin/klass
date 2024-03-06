drop table if exists ASSOCIATION_END;

create table ASSOCIATION_END
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    name varchar(255) not null,
    ordinal int not null,
    owning_class_name varchar(255) not null,
    association_name varchar(255) not null,
    direction varchar(255) not null,
    multiplicity varchar(255) not null,
    result_type_name varchar(255) not null
);

