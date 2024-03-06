drop table if exists PROPERTIES_REQUIRED;

create table PROPERTIES_REQUIRED
(
    properties_required_id bigint not null,
    system_from timestamp not null,
    system_to timestamp not null,
    created_by_id varchar(255) not null,
    created_on timestamp not null,
    last_updated_by_id varchar(255) not null,
    required_string varchar(255) not null,
    required_integer int not null,
    required_long bigint not null,
    required_double float8 not null,
    required_float float4 not null,
    required_boolean boolean not null,
    required_instant timestamp not null,
    required_local_date date not null
);

