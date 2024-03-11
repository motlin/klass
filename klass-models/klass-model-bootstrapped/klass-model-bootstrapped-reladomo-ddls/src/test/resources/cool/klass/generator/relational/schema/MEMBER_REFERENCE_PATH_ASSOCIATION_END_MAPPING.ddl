drop table if exists MEMBER_REFERENCE_PATH_ASSOCIATION_END_MAPPING;

create table MEMBER_REFERENCE_PATH_ASSOCIATION_END_MAPPING
(
    member_reference_path_id bigint not null,
    association_owning_class_name varchar(256) not null,
    association_end_name varchar(256) not null,
    ordinal int not null
);

