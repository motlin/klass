drop table if exists PRIMITIVE_PARAMETER;

create table PRIMITIVE_PARAMETER
(
    id bigint not null,
    ordinal int not null,
    name varchar(256) not null,
    multiplicity varchar(256) not null,
    primitive_type varchar(256) not null
);

