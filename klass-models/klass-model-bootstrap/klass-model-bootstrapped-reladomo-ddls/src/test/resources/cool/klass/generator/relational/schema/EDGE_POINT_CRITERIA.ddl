drop table if exists EDGE_POINT_CRITERIA;

create table EDGE_POINT_CRITERIA
(
    id bigint not null,
    member_reference_path_id bigint not null
);

