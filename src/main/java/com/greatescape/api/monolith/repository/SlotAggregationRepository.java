package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.SlotAggregation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the {@link SlotAggregation} entity.
 */
@Repository
public interface SlotAggregationRepository extends
    JpaRepository<SlotAggregation, UUID>,
    JpaSpecificationExecutor<SlotAggregation>
{
    /*_*/
}
