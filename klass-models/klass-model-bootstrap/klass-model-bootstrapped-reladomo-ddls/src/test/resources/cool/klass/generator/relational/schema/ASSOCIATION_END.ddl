drop table if exists ASSOCIATION_END;

create table ASSOCIATION_END
(
    name varchar(256) not null,
    ordinal int not null,
    owning_class_name varchar(256) not null,
    association_name varchar(256) not null,
    direction varchar(256) not null,
    multiplicity varchar(256) not null,
    result_type_name varchar(256) not null
);

