drop table if exists KLASS;

create table KLASS
(
    name varchar(256) not null,
    ordinal int not null,
    package_name varchar(100000) not null,
    super_class_name varchar(256),
    inheritance_type varchar(256) not null
);

