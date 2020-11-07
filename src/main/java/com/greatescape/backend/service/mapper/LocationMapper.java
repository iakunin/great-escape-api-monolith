package com.greatescape.backend.service.mapper;


import com.greatescape.backend.domain.Location;
import com.greatescape.backend.service.dto.LocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring", uses = {CityMapper.class, MetroMapper.class})
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.title", target = "cityTitle")
    LocationDTO toDto(Location location);

    @Mapping(source = "cityId", target = "city")
    @Mapping(target = "removeMetro", ignore = true)
    Location toEntity(LocationDTO locationDTO);

    default Location fromId(Long id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setId(id);
        return location;
    }
}
