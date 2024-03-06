drop table if exists SHARED_SYNTHETIC_ONE_TO_ONE_SOURCE;

create table SHARED_SYNTHETIC_ONE_TO_ONE_SOURCE
(
    id bigint not null,
    value varchar(255) not null
);

