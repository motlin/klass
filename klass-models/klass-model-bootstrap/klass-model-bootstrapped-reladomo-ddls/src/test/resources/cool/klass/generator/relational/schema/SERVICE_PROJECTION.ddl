drop table if exists SERVICE_PROJECTION;

create table SERVICE_PROJECTION
(
    id bigint not null,
    parent_id bigint,
    class_name varchar(256) not null,
    ordinal int not null,
    name varchar(256) not null,
    package_name varchar(100000) not null
);

