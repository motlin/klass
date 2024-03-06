drop table if exists PARAMETER;

create table PARAMETER
(
    id bigint not null,
    ordinal int not null,
    name varchar(256) not null,
    multiplicity varchar(256) not null
);

