package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.Subscriber;
import com.greatescape.api.monolith.service.dto.SubscriberDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-09T18:02:12+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.9 (Oracle Corporation)"
)
@Component
public class SubscriberMapperImpl implements SubscriberMapper {

    @Override
    public Subscriber toEntity(SubscriberDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Subscriber subscriber = new Subscriber();

        subscriber.setId( dto.getId() );
        subscriber.setName( dto.getName() );
        subscriber.setEmail( dto.getEmail() );

        return subscriber;
    }

    @Override
    public SubscriberDTO toDto(Subscriber entity) {
        if ( entity == null ) {
            return null;
        }

        SubscriberDTO subscriberDTO = new SubscriberDTO();

        subscriberDTO.setId( entity.getId() );
        subscriberDTO.setName( entity.getName() );
        subscriberDTO.setEmail( entity.getEmail() );

        return subscriberDTO;
    }

    @Override
    public List<Subscriber> toEntity(List<SubscriberDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Subscriber> list = new ArrayList<Subscriber>( dtoList.size() );
        for ( SubscriberDTO subscriberDTO : dtoList ) {
            list.add( toEntity( subscriberDTO ) );
        }

        return list;
    }

    @Override
    public List<SubscriberDTO> toDto(List<Subscriber> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SubscriberDTO> list = new ArrayList<SubscriberDTO>( entityList.size() );
        for ( Subscriber subscriber : entityList ) {
            list.add( toDto( subscriber ) );
        }

        return list;
    }
}
