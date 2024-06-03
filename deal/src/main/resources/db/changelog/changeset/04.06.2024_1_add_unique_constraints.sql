-- liquibase formatted sql
-- changeset argus:3

alter table bank.client
    add constraint client_pk
        unique (passport_id);

alter table bank.client
    add constraint client_pk_2
        unique (employment_id);