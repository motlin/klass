drop table if exists PROJECTION_PROJECTION_REFERENCE;

create table PROJECTION_PROJECTION_REFERENCE
(
    name varchar(256) not null,
    ordinal int not null,
    id bigint not null,
    parent_id bigint,
    association_end_class varchar(256) not null,
    association_end_name varchar(256) not null,
    projection_name varchar(256) not null
);

