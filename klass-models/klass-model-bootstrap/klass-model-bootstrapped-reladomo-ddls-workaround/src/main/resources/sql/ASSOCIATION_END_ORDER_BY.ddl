drop table if exists ASSOCIATION_END_ORDER_BY;

create table ASSOCIATION_END_ORDER_BY
(
    association_end_class_name varchar(255) not null,
    association_end_name varchar(255) not null,
    this_member_reference_path_id bigint not null,
    order_by_direction varchar(255) not null
);

