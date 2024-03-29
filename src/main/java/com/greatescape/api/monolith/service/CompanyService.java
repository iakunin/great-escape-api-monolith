package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.service.dto.CompanyDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Company}.
 */
public interface CompanyService {

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    CompanyDTO save(CompanyDTO companyDTO);

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompanyDTO> findAll(Pageable pageable);


    /**
     * Get the "id" company.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompanyDTO> findOne(UUID id);

    /**
     * Delete the "id" company.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
