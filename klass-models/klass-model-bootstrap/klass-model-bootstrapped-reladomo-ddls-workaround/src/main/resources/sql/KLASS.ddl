drop table if exists KLASS;

create table KLASS
(
    name varchar(255) not null,
    super_class_name varchar(255),
    inheritance_type varchar(255) not null
);

