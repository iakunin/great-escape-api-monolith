package com.greatescape.api.monolith.repository;

import com.greatescape.api.monolith.domain.Thematic;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Thematic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThematicRepository
    extends JpaRepository<Thematic, UUID>, JpaSpecificationExecutor<Thematic> {
}
