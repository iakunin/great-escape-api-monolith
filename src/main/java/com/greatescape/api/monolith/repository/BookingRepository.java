package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.Slot;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Booking entity.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>, JpaSpecificationExecutor<Booking> {
    boolean existsBySlot(Slot slot);

    boolean existsBySlotId(UUID slotId);
}
