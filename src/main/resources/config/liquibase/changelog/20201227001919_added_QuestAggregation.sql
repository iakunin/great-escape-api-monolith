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


-- @TODO: Сейчас есть проблема того, что в `quest_aggregation` некорректно отдаётся min_price (без учёта скидки)
--   вариант 1: хранить price_with_discount в slot
--      недостаток: придётся перерассчитывать её при изменении хотя бы одной из скидок (global, company, slot)
--   вариант 2: высчитывать на лету (в некой mat.view)
--              из этой же вьюхи подтягивать при офомрлении брони (сохранять в Booking)
--              из этой же вьюхи отдавать на фронт в /slots
--              из этой же вьюхи отдавать минимальную стоимость брони со скидкой (в /quests)
--      плюсы:
--          - сущность Slot останется иммутабельной
--      минусы:
--          - придётся следить за актуальностью данных во вьюхе
--              - повесить триггеры на пересчёт этих вьюх при обновлении
--                  - глобальных настроек скидок
--                  - настроек скидок Company
--                  - настроек скидок Slot
--                  - запросе слотов у партнёра (если добавились/изменились данные по слотам)
--          - глобальную скидку придётся вынести на уровень БД (в некую таблицу настроек) -- оставить тудуху под это -- сделать позже -- не блочит запуск
--              - но это добавит контроля, т.к. появится возможность версионирования, чего невозможно добиться при пробрасывании скидки через application-config




-- @TODO: maybe `slot_aggregation` mat.view should be created?
--   to calculate `price_with_discount`
--   discount_in_percents
--      - slot
--      - company
--      - global (from appConfig)
