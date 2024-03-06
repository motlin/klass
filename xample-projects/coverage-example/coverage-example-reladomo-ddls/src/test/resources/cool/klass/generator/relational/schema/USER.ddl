drop table if exists USER;

create table USER
(
    user_id varchar(255) not null,
    system_from timestamp not null,
    system_to timestamp not null,
    first_name varchar(255),
    last_name varchar(255),
    email varchar(255)
);

