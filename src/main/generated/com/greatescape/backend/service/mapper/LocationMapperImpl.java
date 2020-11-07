package com.greatescape.backend.service.mapper;

import com.greatescape.backend.domain.City;
import com.greatescape.backend.domain.Location;
import com.greatescape.backend.domain.Metro;
import com.greatescape.backend.service.dto.LocationDTO;
import com.greatescape.backend.service.dto.MetroDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-02T14:27:08+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.9 (Oracle Corporation)"
)
@Component
public class LocationMapperImpl implements LocationMapper {

    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private MetroMapper metroMapper;

    @Override
    public List<Location> toEntity(List<LocationDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Location> list = new ArrayList<Location>( dtoList.size() );
        for ( LocationDTO locationDTO : dtoList ) {
            list.add( toEntity( locationDTO ) );
        }

        return list;
    }

    @Override
    public List<LocationDTO> toDto(List<Location> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<LocationDTO> list = new ArrayList<LocationDTO>( entityList.size() );
        for ( Location location : entityList ) {
            list.add( toDto( location ) );
        }

        return list;
    }

    @Override
    public LocationDTO toDto(Location location) {
        if ( location == null ) {
            return null;
        }

        LocationDTO locationDTO = new LocationDTO();

        locationDTO.setCityTitle( locationCityTitle( location ) );
        locationDTO.setCityId( locationCityId( location ) );
        locationDTO.setId( location.getId() );
        locationDTO.setAddress( location.getAddress() );
        locationDTO.setAddressExplanation( location.getAddressExplanation() );
        locationDTO.setMetros( metroSetToMetroDTOSet( location.getMetros() ) );

        return locationDTO;
    }

    @Override
    public Location toEntity(LocationDTO locationDTO) {
        if ( locationDTO == null ) {
            return null;
        }

        Location location = new Location();

        location.setCity( cityMapper.fromId( locationDTO.getCityId() ) );
        location.setId( locationDTO.getId() );
        location.setAddress( locationDTO.getAddress() );
        location.setAddressExplanation( locationDTO.getAddressExplanation() );
        location.setMetros( metroDTOSetToMetroSet( locationDTO.getMetros() ) );

        return location;
    }

    private String locationCityTitle(Location location) {
        if ( location == null ) {
            return null;
        }
        City city = location.getCity();
        if ( city == null ) {
            return null;
        }
        String title = city.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private Long locationCityId(Location location) {
        if ( location == null ) {
            return null;
        }
        City city = location.getCity();
        if ( city == null ) {
            return null;
        }
        Long id = city.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Set<MetroDTO> metroSetToMetroDTOSet(Set<Metro> set) {
        if ( set == null ) {
            return null;
        }

        Set<MetroDTO> set1 = new HashSet<MetroDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Metro metro : set ) {
            set1.add( metroMapper.toDto( metro ) );
        }

        return set1;
    }

    protected Set<Metro> metroDTOSetToMetroSet(Set<MetroDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Metro> set1 = new HashSet<Metro>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( MetroDTO metroDTO : set ) {
            set1.add( metroMapper.toEntity( metroDTO ) );
        }

        return set1;
    }
}
