-- liquibase formatted sql
-- changeset argus:5

alter table bank.client
    add constraint client_uk
        unique (passport);

alter table bank.client
    add constraint client_uk_2
        unique (employment);

