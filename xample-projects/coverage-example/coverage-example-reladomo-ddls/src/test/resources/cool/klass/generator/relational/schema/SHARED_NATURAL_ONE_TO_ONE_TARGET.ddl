drop table if exists SHARED_NATURAL_ONE_TO_ONE_TARGET;

create table SHARED_NATURAL_ONE_TO_ONE_TARGET
(
    key varchar(255) not null,
    source_key varchar(255) not null,
    value varchar(255) not null
);

