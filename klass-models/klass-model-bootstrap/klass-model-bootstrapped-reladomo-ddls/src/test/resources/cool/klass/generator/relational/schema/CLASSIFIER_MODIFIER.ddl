drop table if exists CLASSIFIER_MODIFIER;

create table CLASSIFIER_MODIFIER
(
    keyword varchar(256) not null,
    ordinal int not null,
    classifier_name varchar(256) not null
);
