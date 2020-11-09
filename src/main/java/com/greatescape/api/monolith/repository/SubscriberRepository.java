package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.Subscriber;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Subscriber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriberRepository
    extends JpaRepository<Subscriber, UUID>, JpaSpecificationExecutor<Subscriber> {
}
