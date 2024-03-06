drop table if exists PROJECTION_ELEMENT;

create table PROJECTION_ELEMENT
(
    id bigint not null,
    parent_id bigint,
    ordinal int not null,
    name varchar(256) not null
);

