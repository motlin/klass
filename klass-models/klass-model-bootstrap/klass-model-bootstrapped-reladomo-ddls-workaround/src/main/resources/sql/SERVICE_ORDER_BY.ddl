drop table if exists SERVICE_ORDER_BY;

create table SERVICE_ORDER_BY
(
    service_class_name varchar(256) not null,
    service_url_string varchar(8192) not null,
    service_verb varchar(256) not null,
    this_member_reference_path_id bigint not null,
    order_by_direction varchar(256) not null
);

