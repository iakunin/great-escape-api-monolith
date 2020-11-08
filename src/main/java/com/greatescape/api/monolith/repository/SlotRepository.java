package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Slot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlotRepository extends JpaRepository<Slot, Long>, JpaSpecificationExecutor<Slot> {
}
