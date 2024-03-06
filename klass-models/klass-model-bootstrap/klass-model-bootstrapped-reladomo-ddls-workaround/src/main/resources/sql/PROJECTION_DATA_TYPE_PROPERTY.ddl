drop table if exists PROJECTION_DATA_TYPE_PROPERTY;

create table PROJECTION_DATA_TYPE_PROPERTY
(
    id bigint not null,
    property_classifier_name varchar(256) not null,
    property_name varchar(256) not null
);

