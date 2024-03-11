drop table if exists URL_PARAMETER;

create table URL_PARAMETER
(
    service_group_name varchar(256) not null,
    url_string varchar(8192) not null,
    parameter_id bigint not null,
    type varchar(255) not null,
    ordinal int not null
);

