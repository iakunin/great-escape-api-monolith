--liquibase formatted sql
--changeset jhipster:20201226232503_1
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
                c.discount_in_percents,
                20
            ) as discount_in_percents,
            coalesce(
                s.commission_in_percents,
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

--rollback drop materialized view "slot_aggregation";
