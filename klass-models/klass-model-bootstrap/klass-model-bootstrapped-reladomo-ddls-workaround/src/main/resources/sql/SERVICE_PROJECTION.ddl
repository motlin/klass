drop table if exists SERVICE_PROJECTION;

create table SERVICE_PROJECTION
(
    id bigint not null,
    package_name varchar(100000) not null,
    class_name varchar(256) not null
);

