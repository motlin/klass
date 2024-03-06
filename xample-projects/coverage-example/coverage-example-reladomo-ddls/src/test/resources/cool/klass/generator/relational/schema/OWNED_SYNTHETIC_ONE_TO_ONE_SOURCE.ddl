drop table if exists OWNED_SYNTHETIC_ONE_TO_ONE_SOURCE;

create table OWNED_SYNTHETIC_ONE_TO_ONE_SOURCE
(
    id bigint not null,
    value varchar(255) not null
);

