drop table if exists URL;

create table URL
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    class_name varchar(256) not null,
    url varchar(8192) not null
);

