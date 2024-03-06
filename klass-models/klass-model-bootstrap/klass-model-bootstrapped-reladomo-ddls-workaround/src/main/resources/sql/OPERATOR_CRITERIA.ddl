drop table if exists OPERATOR_CRITERIA;

create table OPERATOR_CRITERIA
(
    id bigint not null,
    operator varchar(255) not null,
    source_expression_id bigint not null,
    target_expression_id bigint not null
);

