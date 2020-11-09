package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.service.dto.SlotDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-09T18:02:12+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.9 (Oracle Corporation)"
)
@Component
public class SlotMapperImpl implements SlotMapper {

    @Autowired
    private QuestMapper questMapper;

    @Override
    public List<Slot> toEntity(List<SlotDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Slot> list = new ArrayList<Slot>( dtoList.size() );
        for ( SlotDTO slotDTO : dtoList ) {
            list.add( toEntity( slotDTO ) );
        }

        return list;
    }

    @Override
    public List<SlotDTO> toDto(List<Slot> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SlotDTO> list = new ArrayList<SlotDTO>( entityList.size() );
        for ( Slot slot : entityList ) {
            list.add( toDto( slot ) );
        }

        return list;
    }

    @Override
    public SlotDTO toDto(Slot slot) {
        if ( slot == null ) {
            return null;
        }

        SlotDTO slotDTO = new SlotDTO();

        slotDTO.setQuestId( slotQuestId( slot ) );
        slotDTO.setQuestTitle( slotQuestTitle( slot ) );
        slotDTO.setId( slot.getId() );
        slotDTO.setDateTimeLocal( slot.getDateTimeLocal() );
        slotDTO.setDateTimeWithTimeZone( slot.getDateTimeWithTimeZone() );
        slotDTO.setIsAvailable( slot.getIsAvailable() );
        slotDTO.setPrice( slot.getPrice() );
        slotDTO.setDiscountInPercents( slot.getDiscountInPercents() );
        slotDTO.setCommissionInPercents( slot.getCommissionInPercents() );
        slotDTO.setExternalId( slot.getExternalId() );
        slotDTO.setExternalState( slot.getExternalState() );

        return slotDTO;
    }

    @Override
    public Slot toEntity(SlotDTO slotDTO) {
        if ( slotDTO == null ) {
            return null;
        }

        Slot slot = new Slot();

        slot.setQuest( questMapper.fromId( slotDTO.getQuestId() ) );
        slot.setId( slotDTO.getId() );
        slot.setDateTimeLocal( slotDTO.getDateTimeLocal() );
        slot.setDateTimeWithTimeZone( slotDTO.getDateTimeWithTimeZone() );
        slot.setIsAvailable( slotDTO.getIsAvailable() );
        slot.setPrice( slotDTO.getPrice() );
        slot.setDiscountInPercents( slotDTO.getDiscountInPercents() );
        slot.setCommissionInPercents( slotDTO.getCommissionInPercents() );
        slot.setExternalId( slotDTO.getExternalId() );
        slot.setExternalState( slotDTO.getExternalState() );

        return slot;
    }

    private UUID slotQuestId(Slot slot) {
        if ( slot == null ) {
            return null;
        }
        Quest quest = slot.getQuest();
        if ( quest == null ) {
            return null;
        }
        UUID id = quest.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String slotQuestTitle(Slot slot) {
        if ( slot == null ) {
            return null;
        }
        Quest quest = slot.getQuest();
        if ( quest == null ) {
            return null;
        }
        String title = quest.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }
}
