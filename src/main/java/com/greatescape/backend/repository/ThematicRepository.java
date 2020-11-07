package com.greatescape.backend.repository;

import com.greatescape.backend.domain.Thematic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Thematic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThematicRepository extends JpaRepository<Thematic, Long>, JpaSpecificationExecutor<Thematic> {
}
