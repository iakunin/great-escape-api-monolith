package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.service.dto.ThematicDTO;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Thematic} and its DTO {@link ThematicDTO}.
 */
@Mapper(componentModel = "spring")
public interface ThematicMapper extends EntityMapper<ThematicDTO, Thematic> {

    @Mapping(target = "quests", ignore = true)
    @Mapping(target = "removeQuest", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Thematic toEntity(ThematicDTO thematicDTO);

    default Thematic fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Thematic thematic = new Thematic();
        thematic.setId(id);
        return thematic;
    }
}
