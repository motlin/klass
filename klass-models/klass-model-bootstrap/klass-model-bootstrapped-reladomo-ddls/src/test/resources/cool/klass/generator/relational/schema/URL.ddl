drop table if exists URL;

create table URL
(
    service_group_name varchar(256) not null,
    url varchar(8192) not null,
    ordinal int not null
);

