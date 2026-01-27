create table membership_types
(
    id            bigserial primary key,
    name          varchar(100)   not null,
    price         decimal(10, 2) not null,
    duration_days int            not null,
    visit_limit   int            not null
);

create table clients
(
    id    bigserial primary key,
    name  varchar(100) not null,
    phone varchar(20)  not null unique
);

create table client_subscriptions
(
    id         bigserial primary key,
    client_id  bigint references clients (id),
    type_id    bigint references membership_types (id),
    start_date date not null,
    end_date   date not null,
    visit_left int  not null,
    is_active  boolean default true
);

create  table app_users(
    id bigserial primary key,
    username varchar(100) unique  not null,
    password varchar(100) not null,
    role varchar(20) not null
);

INSERT INTO membership_types (name, price, duration_days, visit_limit)
VALUES ('Start (Month)', 15000, 30, 12),
       ('Unlimit (Month)', 25000, 30, 999),
       ('Student (Year)', 90000, 365, 100);

select client_subscriptions.id,clients. from client_subscriptions
    join clients c on client_subscriptions.client_id = c.id