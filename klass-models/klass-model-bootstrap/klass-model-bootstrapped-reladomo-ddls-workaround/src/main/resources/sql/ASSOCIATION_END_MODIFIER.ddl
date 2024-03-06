drop table if exists ASSOCIATION_END_MODIFIER;

create table ASSOCIATION_END_MODIFIER
(
    keyword              varchar(256) NOT NULL,
    ordinal              int          NOT NULL,
    owning_class_name    varchar(256) NOT NULL,
    association_end_name varchar(256) NOT NULL
);
