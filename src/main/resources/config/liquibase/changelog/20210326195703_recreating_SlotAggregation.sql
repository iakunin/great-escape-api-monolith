--liquibase formatted sql
--changeset jhipster:20210326195703_1

drop materialized view quest_aggregation;

drop materialized view slot_aggregation;

create materialized view slot_aggregation as
select tmp2.*,
    (tmp2.price_original - tmp2.discount_absolute) as price_with_discount,
    now() as updated_at
from (
    select tmp1.*,
        cast (
            ceil(
                (tmp1.price_original * tmp1.discount_in_percents) / cast(100 as decimal)
            ) as integer
        ) as discount_absolute,
        cast (
            ceil(
                (tmp1.price_original * tmp1.commission_in_percents) / cast(100 as decimal)
            ) as integer
        ) as commission_absolute
    from (
        select
            s.id,
            s.date_time_local,
            s.date_time_with_time_zone,
            s.is_available,
            s.price as price_original,
            coalesce(
                s.discount_in_percents,
                q.discount_in_percents,
                c.discount_in_percents,
                20
            ) as discount_in_percents,
            coalesce(
                s.commission_in_percents,
                q.commission_in_percents,
                c.commission_in_percents,
                10
            ) as commission_in_percents,
            s.external_id,
            s.external_state,
            s.quest_id,
            s.created_at
        from slot s
        join quest q on q.id = s.quest_id
        join company c on c.id = q.company_id
    ) as tmp1
) as tmp2
;

create materialized view quest_aggregation as
select
    q.id,
    q.slug,
    q.title,
    q.description,
    q.details,
    q.players_min_count,
    q.players_max_count,
    q.duration_in_minutes,
    q.complexity,
    q.fear_level,
    q.type,
    q.cover_photo,
    q.location_id,
    q.company_id,
    q.is_public,
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

--rollback drop materialized view "slot_aggregation";
