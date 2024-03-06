drop table if exists URL_PARAMETER;

create table URL_PARAMETER
(
    url_class_name varchar(256) not null,
    url_string varchar(8192) not null,
    parameter_id bigint not null,
    type varchar(256) not null
);

