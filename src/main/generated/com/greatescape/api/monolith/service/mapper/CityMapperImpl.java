package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.City;
import com.greatescape.api.monolith.service.dto.CityDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-09T19:02:55+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.9 (Oracle Corporation)"
)
@Component
public class CityMapperImpl implements CityMapper {

    @Override
    public CityDTO toDto(City entity) {
        if ( entity == null ) {
            return null;
        }

        CityDTO cityDTO = new CityDTO();

        cityDTO.setId( entity.getId() );
        cityDTO.setSlug( entity.getSlug() );
        cityDTO.setTitle( entity.getTitle() );
        cityDTO.setTimezone( entity.getTimezone() );

        return cityDTO;
    }

    @Override
    public List<City> toEntity(List<CityDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<City> list = new ArrayList<City>( dtoList.size() );
        for ( CityDTO cityDTO : dtoList ) {
            list.add( toEntity( cityDTO ) );
        }

        return list;
    }

    @Override
    public List<CityDTO> toDto(List<City> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<CityDTO> list = new ArrayList<CityDTO>( entityList.size() );
        for ( City city : entityList ) {
            list.add( toDto( city ) );
        }

        return list;
    }

    @Override
    public City toEntity(CityDTO cityDTO) {
        if ( cityDTO == null ) {
            return null;
        }

        City city = new City();

        city.setId( cityDTO.getId() );
        city.setSlug( cityDTO.getSlug() );
        city.setTitle( cityDTO.getTitle() );
        city.setTimezone( cityDTO.getTimezone() );

        return city;
    }
}
