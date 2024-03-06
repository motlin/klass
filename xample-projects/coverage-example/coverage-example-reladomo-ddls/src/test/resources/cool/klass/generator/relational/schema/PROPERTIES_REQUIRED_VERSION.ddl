drop table if exists PROPERTIES_REQUIRED_VERSION;

create table PROPERTIES_REQUIRED_VERSION
(
    properties_required_id bigint not null,
    system_from timestamp not null,
    system_to timestamp not null,
    created_by_id varchar(255) not null,
    created_on timestamp not null,
    last_updated_by_id varchar(255) not null,
    number int not null
);

