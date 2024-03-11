drop table if exists TYPE_MEMBER_REFERENCE_PATH;

create table TYPE_MEMBER_REFERENCE_PATH
(
    id bigint not null,
    class_name varchar(256) not null,
    property_class_name varchar(256) not null,
    property_name varchar(256) not null
);

