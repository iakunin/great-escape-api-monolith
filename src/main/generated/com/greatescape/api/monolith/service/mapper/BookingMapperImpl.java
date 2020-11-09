package com.greatescape.api.monolith.service.mapper;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.service.dto.BookingDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-09T18:50:51+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.9 (Oracle Corporation)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Autowired
    private SlotMapper slotMapper;
    @Autowired
    private QuestMapper questMapper;
    @Autowired
    private PlayerMapper playerMapper;

    @Override
    public List<Booking> toEntity(List<BookingDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Booking> list = new ArrayList<Booking>( dtoList.size() );
        for ( BookingDTO bookingDTO : dtoList ) {
            list.add( toEntity( bookingDTO ) );
        }

        return list;
    }

    @Override
    public List<BookingDTO> toDto(List<Booking> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<BookingDTO> list = new ArrayList<BookingDTO>( entityList.size() );
        for ( Booking booking : entityList ) {
            list.add( toDto( booking ) );
        }

        return list;
    }

    @Override
    public BookingDTO toDto(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setQuestId( bookingQuestId( booking ) );
        bookingDTO.setPlayerPhone( bookingPlayerPhone( booking ) );
        Instant dateTimeLocal = bookingSlotDateTimeLocal( booking );
        if ( dateTimeLocal != null ) {
            bookingDTO.setSlotDateTimeLocal( dateTimeLocal.toString() );
        }
        bookingDTO.setSlotId( bookingSlotId( booking ) );
        bookingDTO.setQuestTitle( bookingQuestTitle( booking ) );
        bookingDTO.setPlayerId( bookingPlayerId( booking ) );
        bookingDTO.setId( booking.getId() );
        bookingDTO.setStatus( booking.getStatus() );
        bookingDTO.setPrice( booking.getPrice() );
        bookingDTO.setDiscountInPercents( booking.getDiscountInPercents() );
        bookingDTO.setCommissionInPercents( booking.getCommissionInPercents() );

        return bookingDTO;
    }

    @Override
    public Booking toEntity(BookingDTO bookingDTO) {
        if ( bookingDTO == null ) {
            return null;
        }

        Booking booking = new Booking();

        booking.setSlot( slotMapper.fromId( bookingDTO.getSlotId() ) );
        booking.setQuest( questMapper.fromId( bookingDTO.getQuestId() ) );
        booking.setPlayer( playerMapper.fromId( bookingDTO.getPlayerId() ) );
        booking.setId( bookingDTO.getId() );
        booking.setStatus( bookingDTO.getStatus() );
        booking.setPrice( bookingDTO.getPrice() );
        booking.setDiscountInPercents( bookingDTO.getDiscountInPercents() );
        booking.setCommissionInPercents( bookingDTO.getCommissionInPercents() );

        return booking;
    }

    private UUID bookingQuestId(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Quest quest = booking.getQuest();
        if ( quest == null ) {
            return null;
        }
        UUID id = quest.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String bookingPlayerPhone(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Player player = booking.getPlayer();
        if ( player == null ) {
            return null;
        }
        String phone = player.getPhone();
        if ( phone == null ) {
            return null;
        }
        return phone;
    }

    private Instant bookingSlotDateTimeLocal(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Slot slot = booking.getSlot();
        if ( slot == null ) {
            return null;
        }
        Instant dateTimeLocal = slot.getDateTimeLocal();
        if ( dateTimeLocal == null ) {
            return null;
        }
        return dateTimeLocal;
    }

    private Long bookingSlotId(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Slot slot = booking.getSlot();
        if ( slot == null ) {
            return null;
        }
        Long id = slot.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String bookingQuestTitle(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Quest quest = booking.getQuest();
        if ( quest == null ) {
            return null;
        }
        String title = quest.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }

    private UUID bookingPlayerId(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Player player = booking.getPlayer();
        if ( player == null ) {
            return null;
        }
        UUID id = player.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
