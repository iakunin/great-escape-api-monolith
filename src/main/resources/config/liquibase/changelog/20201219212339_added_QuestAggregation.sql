--liquibase formatted sql
--changeset jhipster:20201219212339_1
create materialized view quest_aggregation as
select q.*,
    tmp_slot.discount_in_percents,
    tmp_slot.min_price
from quest q
join (
    select sl.quest_id,
        max(sl.discount_in_percents) as discount_in_percents,
        min(sl.price) as min_price
    from slot sl
    group by sl.quest_id
) as tmp_slot on tmp_slot.quest_id = q.id
group by q.id, tmp_slot.discount_in_percents, tmp_slot.min_price
;

--rollback drop materialized view "quest_aggregation";
