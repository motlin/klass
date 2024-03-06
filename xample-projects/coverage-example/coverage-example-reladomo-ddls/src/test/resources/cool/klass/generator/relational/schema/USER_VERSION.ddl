drop table if exists USER_VERSION;

create table USER_VERSION
(
    user_id varchar(255) not null,
    system_from timestamp not null,
    system_to timestamp not null,
    number int not null
);

