drop table if exists OR_CRITERIA;

create table OR_CRITERIA
(
    id bigint not null,
    left_id bigint not null,
    right_id bigint not null
);

