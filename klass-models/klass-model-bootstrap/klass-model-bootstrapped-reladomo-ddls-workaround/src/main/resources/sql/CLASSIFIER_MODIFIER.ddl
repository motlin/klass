drop table if exists CLASSIFIER_MODIFIER;

create table CLASSIFIER_MODIFIER
(
    keyword         varchar(256) NOT NULL,
    ordinal         int          NOT NULL,
    classifier_name varchar(256) NOT NULL
);
