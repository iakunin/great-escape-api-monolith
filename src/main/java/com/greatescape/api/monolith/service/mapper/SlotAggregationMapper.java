package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.SlotAggregation;
import com.greatescape.api.monolith.service.dto.SlotAggregationDTO;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link SlotAggregation} and its DTO {@link SlotAggregationDTO}.
 */
@Mapper(
    componentModel = "spring",
    implementationName="Player<CLASS_NAME>Impl",
    uses = {QuestMapper.class}
    )
public interface SlotAggregationMapper extends EntityMapper<SlotAggregationDTO, SlotAggregation> {

    SlotAggregationDTO toDto(SlotAggregation slot);

    @Mapping(target = "dateTimeWithTimeZone", ignore = true)
    @Mapping(target = "commissionInPercents", ignore = true)
    @Mapping(target = "discountAbsolute", ignore = true)
    @Mapping(target = "commissionAbsolute", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "externalState", ignore = true)
    @Mapping(target = "quest", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SlotAggregation toEntity(SlotAggregationDTO slotDTO);

    default SlotAggregation fromId(UUID id) {
        if (id == null) {
            return null;
        }
        SlotAggregation slot = new SlotAggregation();
        slot.setId(id);
        return slot;
    }
}
