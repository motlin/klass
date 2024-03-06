drop table if exists PROJECTION_DATA_TYPE_PROPERTY;

create table PROJECTION_DATA_TYPE_PROPERTY
(
    name varchar(256) not null,
    ordinal int not null,
    id bigint not null,
    parent_id bigint,
    property_classifier_name varchar(256) not null,
    property_name varchar(256) not null
);

