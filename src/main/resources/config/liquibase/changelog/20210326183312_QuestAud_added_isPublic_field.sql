--liquibase formatted sql
--changeset jhipster:20210326183311_1
alter table "quest_aud"
add column is_public bool;

--rollback alter table "quest_aud" drop column is_public;
