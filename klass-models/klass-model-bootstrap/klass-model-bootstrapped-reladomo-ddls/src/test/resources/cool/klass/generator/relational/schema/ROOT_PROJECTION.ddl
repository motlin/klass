drop table if exists ROOT_PROJECTION;

create table ROOT_PROJECTION
(
    id bigint not null,
    parent_id bigint,
    class_name varchar(256) not null,
    ordinal int not null,
    name varchar(256) not null
);

