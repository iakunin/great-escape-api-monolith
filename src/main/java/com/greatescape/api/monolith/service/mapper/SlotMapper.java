package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.service.dto.SlotDTO;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Slot} and its DTO {@link SlotDTO}.
 */
@Mapper(componentModel = "spring", uses = {QuestMapper.class})
public interface SlotMapper extends EntityMapper<SlotDTO, Slot> {

    @Mapping(source = "quest.id", target = "questId")
    @Mapping(source = "quest.title", target = "questTitle")
    SlotDTO toDto(Slot slot);

    @Mapping(source = "questId", target = "quest")
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
