drop table if exists CLASSIFIER_INTERFACE_MAPPING;

create table CLASSIFIER_INTERFACE_MAPPING
(
    classifier_name varchar(256) not null,
    interface_name varchar(256) not null
);

