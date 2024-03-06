drop table if exists URL_PARAMETER;

create table URL_PARAMETER
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    url_class_name varchar(255) not null,
    url_string varchar(255) not null,
    parameter_id bigint not null,
    type varchar(255) not null
);

