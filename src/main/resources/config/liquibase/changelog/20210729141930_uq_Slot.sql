--liquibase formatted sql
--changeset jhipster:20210729141930_1

create unique index ux_slot_date_time_local
    on slot (date_time_local);

create unique index ux_slot_date_time_with_time_zone
    on slot (date_time_with_time_zone);
