drop table if exists SERVICE_PROJECTION;

create table SERVICE_PROJECTION
(
    name varchar(256) not null,
    ordinal int not null,
    id bigint not null,
    parent_id bigint,
    package_name varchar(100000) not null,
    class_name varchar(256) not null
);

