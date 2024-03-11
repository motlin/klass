drop table if exists ASSOCIATION_END_MODIFIER;

create table ASSOCIATION_END_MODIFIER
(
    owning_class_name varchar(256) not null,
    association_end_name varchar(256) not null,
    keyword varchar(256) not null,
    ordinal int not null
);

