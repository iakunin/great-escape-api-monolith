package com.greatescape.api.monolith.service.mapper;


import com.greatescape.api.monolith.domain.Subscriber;
import com.greatescape.api.monolith.service.dto.SubscriberDTO;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Subscriber} and its DTO {@link SubscriberDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriberMapper extends EntityMapper<SubscriberDTO, Subscriber> {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Subscriber toEntity(SubscriberDTO subscriberDTO);

    default Subscriber fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Subscriber subscriber = new Subscriber();
        subscriber.setId(id);
        return subscriber;
    }
}
