--liquibase formatted sql
--changeset jhipster:20210326195701_1
alter table "quest"
add column commission_in_percents integer default null;

--rollback alter table "quest" drop column commission_in_percents;
