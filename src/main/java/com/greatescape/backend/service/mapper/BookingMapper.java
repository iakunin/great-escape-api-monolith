package com.greatescape.backend.service.mapper;


import com.greatescape.backend.domain.Booking;
import com.greatescape.backend.service.dto.BookingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring", uses = {SlotMapper.class, QuestMapper.class, PlayerMapper.class})
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {

    @Mapping(source = "slot.id", target = "slotId")
    @Mapping(source = "slot.dateTimeLocal", target = "slotDateTimeLocal")
    @Mapping(source = "quest.id", target = "questId")
    @Mapping(source = "quest.title", target = "questTitle")
    @Mapping(source = "player.id", target = "playerId")
    @Mapping(source = "player.phone", target = "playerPhone")
    BookingDTO toDto(Booking booking);

    @Mapping(source = "slotId", target = "slot")
    @Mapping(source = "questId", target = "quest")
    @Mapping(source = "playerId", target = "player")
    Booking toEntity(BookingDTO bookingDTO);

    default Booking fromId(Long id) {
        if (id == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }
}
