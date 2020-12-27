--liquibase formatted sql
--changeset jhipster:20201227001919_1
create materialized view quest_aggregation as
select
    q.id,
    q.slug,
    q.title,
    q.description,
    q.players_min_count,
    q.players_max_count,
    q.duration_in_minutes,
    q.complexity,
    q.fear_level,
    q.type,
    q.cover_photo,
    q.location_id,
    q.company_id,
    q.created_at,
    now() as updated_at,
    tmp_slot.discount_in_percents,
    tmp_slot.min_price
from quest q
join (
    select s.quest_id,
        max(s.discount_in_percents) as discount_in_percents,
        min(s.price_with_discount) as min_price
    from slot_aggregation s
    group by s.quest_id
) as tmp_slot on tmp_slot.quest_id = q.id
group by q.id, tmp_slot.discount_in_percents, tmp_slot.min_price
;

--rollback drop materialized view "quest_aggregation";
