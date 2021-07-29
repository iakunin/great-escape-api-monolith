--liquibase formatted sql
--changeset jhipster:20210729141930_1

create unique index ux__slot__quest_id__date_time_local
    on slot (quest_id, date_time_local);

create unique index ux__slot__quest_id__date_time_with_time_zone
    on slot (quest_id, date_time_with_time_zone);
