package com.greatescape.backend.service.mapper;


import com.greatescape.backend.domain.Subscriber;
import com.greatescape.backend.service.dto.SubscriberDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Subscriber} and its DTO {@link SubscriberDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriberMapper extends EntityMapper<SubscriberDTO, Subscriber> {



    default Subscriber fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subscriber subscriber = new Subscriber();
        subscriber.setId(id);
        return subscriber;
    }
}
