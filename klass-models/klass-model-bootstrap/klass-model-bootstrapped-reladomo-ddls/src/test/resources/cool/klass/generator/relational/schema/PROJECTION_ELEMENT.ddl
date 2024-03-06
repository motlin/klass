drop table if exists PROJECTION_ELEMENT;

create table PROJECTION_ELEMENT
(
    name varchar(256) not null,
    id bigint not null,
    parent_id bigint,
    ordinal int not null
);

