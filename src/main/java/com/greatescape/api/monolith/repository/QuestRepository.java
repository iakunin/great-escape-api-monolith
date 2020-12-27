package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.Location;
import com.greatescape.api.monolith.domain.Quest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Quest entity.
 */
@Repository
public interface QuestRepository extends JpaRepository<Quest, UUID>, JpaSpecificationExecutor<Quest> {

    @Query(value = "select distinct quest from Quest quest left join fetch quest.thematics",
        countQuery = "select count(distinct quest) from Quest quest")
    Page<Quest> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct quest from Quest quest left join fetch quest.thematics")
    List<Quest> findAllWithEagerRelationships();

    @Query("select quest from Quest quest left join fetch quest.thematics where quest.id =:id")
    Optional<Quest> findOneWithEagerRelationships(@Param("id") UUID id);

    List<Quest> findAllByLocation(Location location);
}
