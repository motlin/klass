drop table if exists PROJECTION_ELEMENT;

create table PROJECTION_ELEMENT
(
    ordinal int not null,
    name varchar(256) not null,
    id bigint not null,
    parent_id bigint
);

