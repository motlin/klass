drop table if exists PROJECTION_WITH_ASSOCIATION_END;

create table PROJECTION_WITH_ASSOCIATION_END
(
    id bigint not null,
    parent_id bigint,
    association_end_class varchar(256) not null,
    association_end_name varchar(256) not null,
    ordinal int not null,
    name varchar(256) not null
);

