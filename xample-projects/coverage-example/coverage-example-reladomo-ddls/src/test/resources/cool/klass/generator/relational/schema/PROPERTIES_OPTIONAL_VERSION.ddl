drop table if exists PROPERTIES_OPTIONAL_VERSION;

create table PROPERTIES_OPTIONAL_VERSION
(
    properties_optional_id bigint not null,
    number int not null,
    system_from timestamp not null,
    system_to timestamp not null,
    created_by_id varchar(255) not null,
    created_on timestamp not null,
    last_updated_by_id varchar(255) not null
);

