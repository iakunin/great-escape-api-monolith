--liquibase formatted sql
--changeset jhipster:20210326183311_1
alter table "quest"
add column is_public bool default true;

--rollback alter table "quest" drop column is_public;
