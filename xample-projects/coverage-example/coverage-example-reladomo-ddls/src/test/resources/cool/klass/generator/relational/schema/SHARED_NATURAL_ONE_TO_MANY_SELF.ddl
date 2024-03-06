drop table if exists SHARED_NATURAL_ONE_TO_MANY_SELF;

create table SHARED_NATURAL_ONE_TO_MANY_SELF
(
    key varchar(255) not null,
    source_key varchar(255),
    value varchar(255) not null
);

