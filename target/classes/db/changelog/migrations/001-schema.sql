--liquibase formatted sql
--changeset Elyne:001
--validCheckSum: 8:cb5320983b2b5e45190a7df63747490f
--preconditions onFail:CONTINUE onError:HALT
--precondition-sql-check expectedResult:0 SELECT count(*) FROM information_schema.tables where table_name = 'users';

create table users
(
    id       serial
        constraint users_pkey
            primary key,
    login    varchar(30) not null
        constraint users_login_key
            unique,
    password varchar(255) not null
);

create table storage
(
    id      bigserial
        constraint storage_pkey
            primary key,
    data    oid,
    name    varchar(255),
    type    varchar(255),
    user_id bigint not null
        constraint user_id
            references users,
    size    bigint
);

create table roles
(
    id   serial
        constraint roles_pkey
            primary key,
    name varchar(30) not null
        constraint roles_name_key
            unique
);

create table user_roles
(
    user_id bigint not null
        constraint user_id
            references users,
    role_id bigint not null
        constraint role_id
            references roles,
    constraint user_role
        primary key (user_id, role_id)
);

alter table users
    owner to postgres;

alter table user_roles
    owner to postgres;

alter table roles
    owner to postgres;

alter table storage
    owner to postgres;