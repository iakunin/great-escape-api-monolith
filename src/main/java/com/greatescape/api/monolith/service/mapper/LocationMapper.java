package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.Location;
import com.greatescape.api.monolith.service.dto.LocationDTO;
import java.util.UUID;
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
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Location toEntity(LocationDTO locationDTO);

    default Location fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setId(id);
        return location;
    }
}
