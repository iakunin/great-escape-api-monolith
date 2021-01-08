--liquibase formatted sql
--changeset jhipster:20210107021728_1
create table "otp"
(
    "id"              uuid primary key,
    "code"            varchar not null,
    "number"          int     not null,
    "status"          varchar not null,
    "payload"         varchar,
    "expiration_date" timestamp with time zone,
    "created_at"      timestamp with time zone,
    "updated_at"      timestamp with time zone
);

--rollback drop table "otp";
