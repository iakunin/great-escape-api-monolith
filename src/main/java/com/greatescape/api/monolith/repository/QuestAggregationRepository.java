package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.QuestAggregation;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the {@link QuestAggregation} entity.
 */
@Repository
public interface QuestAggregationRepository extends
    JpaRepository<QuestAggregation, UUID>,
    JpaSpecificationExecutor<QuestAggregation>
{
    Optional<QuestAggregation> findOneBySlug(String slug);
}
