drop table if exists CLASSIFIER_MODIFIER;

create table CLASSIFIER_MODIFIER
(
    classifier_name varchar(256) not null,
    keyword varchar(256) not null,
    ordinal int not null
);

