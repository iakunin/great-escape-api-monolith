--liquibase formatted sql
--changeset jhipster:20201219212339_1
CREATE MATERIALIZED VIEW quest_aggregation as
select q.*,
       c.discount_in_percents,
       tmp_slot.min_price,
       tmp_quest_photo.url as cover_image
from quest q
         join company c on q.company_id = c.id
         join (
    select sl.quest_id, min(sl.price) as min_price from slot sl group by sl.quest_id
) as tmp_slot on tmp_slot.quest_id = q.id
         join (
    select min(qp.created_at), qp.quest_id, qp.url
    from quest_photo qp
    group by qp.quest_id, qp.url
) as tmp_quest_photo on tmp_quest_photo.quest_id = q.id
;

--rollback DROP MATERIALIZED VIEW "quest_aggregation";
