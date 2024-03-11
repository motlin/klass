drop table if exists PROJECTION_PROJECTION_REFERENCE;

create table PROJECTION_PROJECTION_REFERENCE
(
    id bigint not null,
    parent_id bigint,
    association_end_class varchar(256) not null,
    association_end_name varchar(256) not null,
    projection_name varchar(256) not null,
    ordinal int not null,
    name varchar(256) not null
);

