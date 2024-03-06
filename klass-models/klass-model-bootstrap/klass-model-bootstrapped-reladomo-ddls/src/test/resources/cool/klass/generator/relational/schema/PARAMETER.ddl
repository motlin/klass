drop table if exists PARAMETER;

create table PARAMETER
(
    name varchar(256) not null,
    ordinal int not null,
    id bigint not null,
    multiplicity varchar(256) not null
);

