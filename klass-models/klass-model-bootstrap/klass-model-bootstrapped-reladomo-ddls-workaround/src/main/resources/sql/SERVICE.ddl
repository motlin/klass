drop table if exists SERVICE;

create table SERVICE
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    class_name varchar(256) not null,
    url_string varchar(8192) not null,
    verb varchar(256) not null,
    service_multiplicity varchar(256) not null,
    projection_name varchar(256),
    query_criteria_id bigint,
    authorize_criteria_id bigint,
    validate_criteria_id bigint,
    conflict_criteria_id bigint
);

