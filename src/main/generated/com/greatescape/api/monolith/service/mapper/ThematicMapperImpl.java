package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.service.dto.ThematicDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-09T16:48:55+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.8 (JetBrains s.r.o.)"
)
@Component
public class ThematicMapperImpl implements ThematicMapper {

    @Override
    public ThematicDTO toDto(Thematic entity) {
        if ( entity == null ) {
            return null;
        }

        ThematicDTO thematicDTO = new ThematicDTO();

        thematicDTO.setId( entity.getId() );
        thematicDTO.setSlug( entity.getSlug() );
        thematicDTO.setTitle( entity.getTitle() );

        return thematicDTO;
    }

    @Override
    public List<Thematic> toEntity(List<ThematicDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Thematic> list = new ArrayList<Thematic>( dtoList.size() );
        for ( ThematicDTO thematicDTO : dtoList ) {
            list.add( toEntity( thematicDTO ) );
        }

        return list;
    }

    @Override
    public List<ThematicDTO> toDto(List<Thematic> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ThematicDTO> list = new ArrayList<ThematicDTO>( entityList.size() );
        for ( Thematic thematic : entityList ) {
            list.add( toDto( thematic ) );
        }

        return list;
    }

    @Override
    public Thematic toEntity(ThematicDTO thematicDTO) {
        if ( thematicDTO == null ) {
            return null;
        }

        Thematic thematic = new Thematic();

        thematic.setId( thematicDTO.getId() );
        thematic.setSlug( thematicDTO.getSlug() );
        thematic.setTitle( thematicDTO.getTitle() );

        return thematic;
    }
}
