--liquibase formatted sql
--changeset Elyne:001
--validCheckSum: 8:bf219c22ac02e7c1daec889b03f835b7
--preconditions onFail:CONTINUE onError:HALT
--precondition-sql-check expectedResult:0 SELECT count(*) FROM users;

insert into users (login, password)
values ('Elyne@gmail.com', '$2a$09$lGLoHmbdAusmMX/BnRV6defnPQN937UaEL//l0Rld5XvDrwe3kx/W'),
       ('User1@mail.ru', '$2a$10$T2/QpCZf8kp5Xq1aMb.hyuC0G9Dr7/PzHqEE5/BYY2PCGA83Oq9.W');

insert into roles (name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');

insert into user_roles (user_id, role_id)
values (1, 2),
       (2, 1);
