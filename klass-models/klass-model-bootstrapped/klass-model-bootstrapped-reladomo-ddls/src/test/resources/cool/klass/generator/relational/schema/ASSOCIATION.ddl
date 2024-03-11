drop table if exists ASSOCIATION;

create table ASSOCIATION
(
    name varchar(256) not null,
    criteria_id bigint not null,
    ordinal int not null,
    package_name varchar(100000) not null
);

