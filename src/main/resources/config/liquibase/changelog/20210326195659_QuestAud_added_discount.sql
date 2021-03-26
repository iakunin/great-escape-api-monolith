--liquibase formatted sql
--changeset jhipster:20210326195659_1
alter table "quest_aud"
add column discount_in_percents integer;

--rollback alter table "quest_aud" drop column discount_in_percents;
