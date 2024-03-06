drop table if exists KLASS;

create table KLASS
(
    name varchar(256) not null,
    super_class_name varchar(256),
    ordinal int not null,
    package_name varchar(100000) not null,
    abstract_class boolean not null
);

