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

insert into users (login, password)
values ('Elyne@gmail.com', '$2a$09$lGLoHmbdAusmMX/BnRV6defnPQN937UaEL//l0Rld5XvDrwe3kx/W'),
       ('User1@mail.ru', '$2a$10$T2/QpCZf8kp5Xq1aMb.hyuC0G9Dr7/PzHqEE5/BYY2PCGA83Oq9.W');

insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into user_roles (user_id, role_id)
values (1, 2),
       (2, 1);

