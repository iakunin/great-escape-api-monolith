package com.greatescape.backend.service.mapper;


import com.greatescape.backend.domain.Slot;
import com.greatescape.backend.service.dto.SlotDTO;
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
    Slot toEntity(SlotDTO slotDTO);

    default Slot fromId(Long id) {
        if (id == null) {
            return null;
        }
        Slot slot = new Slot();
        slot.setId(id);
        return slot;
    }
}
