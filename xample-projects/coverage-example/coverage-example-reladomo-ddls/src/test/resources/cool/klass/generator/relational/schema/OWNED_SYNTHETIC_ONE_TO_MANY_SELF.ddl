drop table if exists OWNED_SYNTHETIC_ONE_TO_MANY_SELF;

create table OWNED_SYNTHETIC_ONE_TO_MANY_SELF
(
    id bigint not null,
    source_id bigint,
    value varchar(255) not null
);

