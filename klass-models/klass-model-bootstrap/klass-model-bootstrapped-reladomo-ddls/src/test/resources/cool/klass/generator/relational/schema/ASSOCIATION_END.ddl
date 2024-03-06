drop table if exists ASSOCIATION_END;

create table ASSOCIATION_END
(
    owning_class_name varchar(256) not null,
    name varchar(256) not null,
    association_name varchar(256) not null,
    result_type_name varchar(256) not null,
    ordinal int not null,
    direction varchar(256) not null,
    multiplicity varchar(256) not null
);

