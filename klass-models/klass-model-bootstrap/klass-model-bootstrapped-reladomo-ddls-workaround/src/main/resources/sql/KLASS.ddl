drop table if exists KLASS;

create table KLASS
(
    name varchar(256) not null,
    super_class_name varchar(256),
    inheritance_type varchar(256) not null
);

