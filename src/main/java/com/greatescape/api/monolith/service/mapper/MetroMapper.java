package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.Metro;
import com.greatescape.api.monolith.service.dto.MetroDTO;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Metro} and its DTO {@link MetroDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MetroMapper extends EntityMapper<MetroDTO, Metro> {


    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "removeLocation", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Metro toEntity(MetroDTO metroDTO);

    default Metro fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Metro metro = new Metro();
        metro.setId(id);
        return metro;
    }
}
