drop table if exists SHARED_SYNTHETIC_ONE_TO_MANY_TARGET;

create table SHARED_SYNTHETIC_ONE_TO_MANY_TARGET
(
    id bigint not null,
    source_id bigint not null,
    value varchar(255) not null
);

