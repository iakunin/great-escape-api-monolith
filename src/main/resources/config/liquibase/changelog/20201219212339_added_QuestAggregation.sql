--liquibase formatted sql
--changeset jhipster:20201219212339_1
create materialized view quest_aggregation as
select q.*,
    c.discount_in_percents,
    tmp_slot.min_price
from quest q
join company c on q.company_id = c.id
join (
    select sl.quest_id, min(sl.price) as min_price
    from slot sl
    group by sl.quest_id
) as tmp_slot on tmp_slot.quest_id = q.id
group by q.id, c.discount_in_percents, tmp_slot.min_price
;

--rollback drop materialized view "quest_aggregation";
