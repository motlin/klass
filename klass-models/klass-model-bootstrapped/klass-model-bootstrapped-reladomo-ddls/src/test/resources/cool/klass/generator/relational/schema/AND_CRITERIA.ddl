drop table if exists AND_CRITERIA;

create table AND_CRITERIA
(
    id bigint not null,
    left_id bigint not null,
    right_id bigint not null
);

