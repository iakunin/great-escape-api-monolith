--liquibase formatted sql
--changeset jhipster:20210326195702_1
alter table "quest_aud"
add column commission_in_percents integer default null;

--rollback alter table "quest_aud" drop column commission_in_percents;
