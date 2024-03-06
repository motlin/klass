drop table if exists PROPERTIES_OPTIONAL;

create table PROPERTIES_OPTIONAL
(
    properties_optional_id bigint not null,
    optional_string varchar(255),
    optional_integer int,
    optional_long bigint,
    optional_double float8,
    optional_float float4,
    optional_boolean boolean,
    optional_instant timestamp,
    optional_local_date date,
    system_from timestamp not null,
    system_to timestamp not null,
    created_by_id varchar(255) not null,
    created_on timestamp not null,
    last_updated_by_id varchar(255) not null
);

