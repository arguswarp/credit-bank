-- liquibase formatted sql
-- changeset argus:4

drop table if exists bank.passport cascade ;

drop table if exists bank.employment cascade ;

alter table bank.client
    drop column passport_id;

alter table bank.client
    drop column employment_id;

alter table bank.client
    add column passport jsonb;

alter table bank.client
    add column employment jsonb;