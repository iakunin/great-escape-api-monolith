package com.greatescape.backend.repository;

import com.greatescape.backend.domain.Metro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Metro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetroRepository extends JpaRepository<Metro, Long>, JpaSpecificationExecutor<Metro> {
}
