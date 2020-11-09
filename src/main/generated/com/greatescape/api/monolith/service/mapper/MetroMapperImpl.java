package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.Metro;
import com.greatescape.api.monolith.service.dto.MetroDTO;
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
public class MetroMapperImpl implements MetroMapper {

    @Override
    public MetroDTO toDto(Metro entity) {
        if ( entity == null ) {
            return null;
        }

        MetroDTO metroDTO = new MetroDTO();

        metroDTO.setId( entity.getId() );
        metroDTO.setSlug( entity.getSlug() );
        metroDTO.setTitle( entity.getTitle() );

        return metroDTO;
    }

    @Override
    public List<Metro> toEntity(List<MetroDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Metro> list = new ArrayList<Metro>( dtoList.size() );
        for ( MetroDTO metroDTO : dtoList ) {
            list.add( toEntity( metroDTO ) );
        }

        return list;
    }

    @Override
    public List<MetroDTO> toDto(List<Metro> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<MetroDTO> list = new ArrayList<MetroDTO>( entityList.size() );
        for ( Metro metro : entityList ) {
            list.add( toDto( metro ) );
        }

        return list;
    }

    @Override
    public Metro toEntity(MetroDTO metroDTO) {
        if ( metroDTO == null ) {
            return null;
        }

        Metro metro = new Metro();

        metro.setId( metroDTO.getId() );
        metro.setSlug( metroDTO.getSlug() );
        metro.setTitle( metroDTO.getTitle() );

        return metro;
    }
}
