drop table if exists CRITERIA;

create table CRITERIA
(
    inferred boolean not null,
    source_code varchar(100000) not null,
    source_code_with_inference varchar(100000) not null,
    id bigint not null
);

