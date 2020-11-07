package com.greatescape.backend.service.mapper;


import com.greatescape.backend.domain.Thematic;
import com.greatescape.backend.service.dto.ThematicDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Thematic} and its DTO {@link ThematicDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ThematicMapper extends EntityMapper<ThematicDTO, Thematic> {


    @Mapping(target = "quests", ignore = true)
    @Mapping(target = "removeQuest", ignore = true)
    Thematic toEntity(ThematicDTO thematicDTO);

    default Thematic fromId(Long id) {
        if (id == null) {
            return null;
        }
        Thematic thematic = new Thematic();
        thematic.setId(id);
        return thematic;
    }
}
