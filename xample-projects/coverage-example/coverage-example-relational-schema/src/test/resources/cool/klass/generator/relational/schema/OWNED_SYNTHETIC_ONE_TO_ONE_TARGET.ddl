drop table if exists OWNED_SYNTHETIC_ONE_TO_ONE_TARGET;

create table OWNED_SYNTHETIC_ONE_TO_ONE_TARGET
(
    id bigint not null,
    source_id bigint not null,
    value varchar(255) not null
);

