package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.Location;
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
 * Spring Data  repository for the Location entity.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, UUID>, JpaSpecificationExecutor<Location> {

    @Query(value = "select distinct location from Location location left join fetch location.metros",
        countQuery = "select count(distinct location) from Location location")
    Page<Location> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct location from Location location left join fetch location.metros")
    List<Location> findAllWithEagerRelationships();

    @Query("select location from Location location left join fetch location.metros where location.id =:id")
    Optional<Location> findOneWithEagerRelationships(@Param("id") UUID id);
}
