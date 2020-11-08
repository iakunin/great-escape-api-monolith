package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.City;
import com.greatescape.api.monolith.service.dto.CityDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CityMapper extends EntityMapper<CityDTO, City> {



    default City fromId(Long id) {
        if (id == null) {
            return null;
        }
        City city = new City();
        city.setId(id);
        return city;
    }
}
