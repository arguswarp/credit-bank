-- liquibase formatted sql
-- changeset argus:2

alter table bank.passport
    alter column issue_branch drop not null;

alter table bank.passport
    alter column issue_date drop not null;

alter table bank.client
    alter column gender drop not null;

alter table bank.client
    alter column marital_status drop not null;

alter table bank.client
    alter column account_number drop not null;
