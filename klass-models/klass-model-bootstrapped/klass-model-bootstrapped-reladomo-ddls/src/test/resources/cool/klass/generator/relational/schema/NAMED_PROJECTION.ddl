drop table if exists NAMED_PROJECTION;

create table NAMED_PROJECTION
(
    name varchar(256) not null,
    projection_id bigint not null,
    ordinal int not null,
    package_name varchar(100000) not null
);

