drop table if exists SERVICE;

create table SERVICE
(
    class_name varchar(256) not null,
    url_string varchar(8192) not null,
    verb varchar(256) not null,
    projection_name varchar(256),
    query_criteria_id bigint,
    authorize_criteria_id bigint,
    validate_criteria_id bigint,
    conflict_criteria_id bigint,
    service_multiplicity varchar(256) not null,
    ordinal int not null
);

