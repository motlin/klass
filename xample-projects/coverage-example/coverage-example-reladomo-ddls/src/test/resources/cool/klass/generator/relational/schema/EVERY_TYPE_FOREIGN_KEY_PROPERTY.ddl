drop table if exists EVERY_TYPE_FOREIGN_KEY_PROPERTY;

create table EVERY_TYPE_FOREIGN_KEY_PROPERTY
(
    id bigint not null,
    foreign_key_string varchar(255) not null,
    foreign_key_integer int not null,
    foreign_key_long bigint not null,
    foreign_key_double float8 not null,
    foreign_key_float float4 not null,
    foreign_key_boolean boolean not null,
    foreign_key_instant timestamp not null,
    foreign_key_local_date date not null,
    data varchar(255) not null,
    system_from timestamp not null,
    system_to timestamp not null
);

