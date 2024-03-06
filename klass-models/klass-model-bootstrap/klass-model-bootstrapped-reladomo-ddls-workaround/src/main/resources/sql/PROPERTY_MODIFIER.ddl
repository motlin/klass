drop table if exists PROPERTY_MODIFIER;

create table PROPERTY_MODIFIER
(
    keyword         varchar(256) NOT NULL,
    ordinal         int          NOT NULL,
    classifier_name varchar(256) NOT NULL,
    property_name   varchar(256) NOT NULL
);
