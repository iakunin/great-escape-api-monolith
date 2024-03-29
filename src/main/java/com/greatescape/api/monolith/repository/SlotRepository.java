package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.Slot;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Slot entity.
 */
@Repository
public interface SlotRepository extends JpaRepository<Slot, UUID>, JpaSpecificationExecutor<Slot> {
    List<Slot> findAllByQuest(Quest quest);
}
