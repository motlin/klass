drop table if exists PARAMETERIZED_PROPERTY_ORDERING;

create table PARAMETERIZED_PROPERTY_ORDERING
(
    owning_class_name varchar(256) not null,
    name varchar(256) not null,
    ordering_id bigint not null
);

