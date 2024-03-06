drop table if exists URL;

create table URL
(
    class_name varchar(256) not null,
    url varchar(8192) not null
);

