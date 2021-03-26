--liquibase formatted sql
--changeset jhipster:20210326195658_1
alter table "quest"
add column discount_in_percents integer default null;

--rollback alter table "quest" drop column discount_in_percents;
