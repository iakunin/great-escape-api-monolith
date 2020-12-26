package com.greatescape.api.monolith.service.mapper.player;


import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.service.dto.player.SlotDTO;
import com.greatescape.api.monolith.service.mapper.EntityMapper;
import com.greatescape.api.monolith.service.mapper.QuestMapper;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Slot} and its DTO {@link SlotDTO}.
 */
@Mapper(
    componentModel = "spring",
    implementationName="Player<CLASS_NAME>Impl",
    uses = {QuestMapper.class}
    )
public interface SlotMapper extends EntityMapper<SlotDTO, Slot> {

    SlotDTO toDto(Slot slot);

    @Mapping(target = "dateTimeWithTimeZone", ignore = true)
    @Mapping(target = "commissionInPercents", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "externalState", ignore = true)
    @Mapping(target = "quest", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Slot toEntity(SlotDTO slotDTO);

    default Slot fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Slot slot = new Slot();
        slot.setId(id);
        return slot;
    }
}
