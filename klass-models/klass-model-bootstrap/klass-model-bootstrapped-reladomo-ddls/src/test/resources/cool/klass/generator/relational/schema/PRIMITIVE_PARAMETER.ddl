drop table if exists PRIMITIVE_PARAMETER;

create table PRIMITIVE_PARAMETER
(
    name varchar(256) not null,
    ordinal int not null,
    id bigint not null,
    multiplicity varchar(256) not null,
    primitive_type varchar(256) not null
);

