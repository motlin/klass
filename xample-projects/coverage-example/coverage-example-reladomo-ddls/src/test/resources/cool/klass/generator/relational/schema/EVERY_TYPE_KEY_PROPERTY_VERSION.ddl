drop table if exists EVERY_TYPE_KEY_PROPERTY_VERSION;

create table EVERY_TYPE_KEY_PROPERTY_VERSION
(
    key_string varchar(255) not null,
    key_integer int not null,
    key_long bigint not null,
    key_double float8 not null,
    key_float float4 not null,
    key_boolean boolean not null,
    key_instant timestamp not null,
    key_local_date date not null,
    system_from timestamp not null,
    system_to timestamp not null,
    created_by_id varchar(255) not null,
    created_on timestamp not null,
    last_updated_by_id varchar(255) not null,
    number int not null
);

