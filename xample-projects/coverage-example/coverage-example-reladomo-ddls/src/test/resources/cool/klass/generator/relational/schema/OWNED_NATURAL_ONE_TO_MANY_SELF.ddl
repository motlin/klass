drop table if exists OWNED_NATURAL_ONE_TO_MANY_SELF;

create table OWNED_NATURAL_ONE_TO_MANY_SELF
(
    key varchar(255) not null,
    source_key varchar(255),
    value varchar(255) not null
);

