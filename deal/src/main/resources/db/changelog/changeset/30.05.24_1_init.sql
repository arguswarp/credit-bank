-- liquibase formatted sql
-- changeset argus:1

create schema bank;

create table bank.passport
(
    passport_id  uuid primary key,
    series       varchar(4)  not null,
    number       varchar(6)  not null,
    issue_branch varchar(100) not null,
    issue_date   date        not null
);

create table bank.employment
(
    employment_id           uuid primary key,
    status                  varchar(20) not null,
    employer_inn            varchar(10),
    salary                  decimal check ( salary > 0 ),
    position                varchar,
    work_experience_total   int check ( work_experience_total >= 0 ),
    work_experience_current int check ( work_experience_current >= 0 )
);

create table bank.client
(
    client_id        uuid primary key,
    last_name        varchar(30) not null,
    first_name       varchar(30) not null,
    middle_name      varchar(30),
    birth_date       date        not null,
    email            varchar(50) not null,
    gender           varchar(10) not null,
    marital_status   varchar(20) not null,
    dependent_amount int check ( dependent_amount >= 0 ),
    passport_id      uuid references bank.passport (passport_id),
    employment_id    uuid references bank.employment (employment_id),
    account_number   varchar(20) not null
);

create table bank.credit
(
    credit_id         uuid primary key,
    amount            decimal check ( amount >= 30000 )     not null,
    term              int check ( term >= 6 )               not null,
    monthly_payment   decimal check ( monthly_payment > 0 ) not null,
    rate              decimal check ( rate > 0 )            not null,
    psk               decimal check ( psk > 0 )             not null,
    payment_schedule  jsonb                                 not null,
    insurance_enabled boolean                               not null,
    salary_client     boolean                               not null,
    credit_status     varchar(15)                           not null
);

create table bank.statement
(
    statement_id   uuid primary key not null,
    client_id      uuid references bank.client (client_id),
    credit_id      uuid references bank.credit (credit_id),
    status         varchar(30)      not null,
    creation_date  timestamp        not null,
    applied_offer  jsonb,
    sign_date      timestamp,
    ses_code       varchar(50),
    status_history jsonb
);
