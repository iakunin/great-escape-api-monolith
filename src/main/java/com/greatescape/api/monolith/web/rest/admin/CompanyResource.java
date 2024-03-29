package com.greatescape.api.monolith.web.rest.admin;

import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.service.CompanyQueryService;
import com.greatescape.api.monolith.service.CompanyService;
import com.greatescape.api.monolith.service.dto.CompanyCriteria;
import com.greatescape.api.monolith.service.dto.CompanyDTO;
import com.greatescape.api.monolith.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link Company}.
 */
@RestController
@RequestMapping("/admin-api")
@RequiredArgsConstructor
@Slf4j
public class CompanyResource {

    private static final String ENTITY_NAME = "company";

    @Value("${jhipster.clientApp.name}")
    private final String applicationName;

    private final CompanyService companyService;

    private final CompanyQueryService companyQueryService;

    /**
     * {@code POST  /companies} : Create a new company.
     *
     * @param companyDTO the companyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyDTO, or with status {@code 400 (Bad Request)} if the company has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/companies")
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO companyDTO) throws URISyntaxException {
        log.debug("REST request to save Company : {}", companyDTO);
        if (companyDTO.getId() != null) {
            throw new BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyDTO result = companyService.save(companyDTO);
        return ResponseEntity.created(new URI("/admin-api/companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /companies} : Updates an existing company.
     *
     * @param companyDTO the companyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDTO,
     * or with status {@code 400 (Bad Request)} if the companyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/companies")
    public ResponseEntity<CompanyDTO> updateCompany(@Valid @RequestBody CompanyDTO companyDTO) throws URISyntaxException {
        log.debug("REST request to update Company : {}", companyDTO);
        if (companyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompanyDTO result = companyService.save(companyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /companies} : get all the companies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companies in body.
     */
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies(CompanyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Companies by criteria: {}", criteria);
        Page<CompanyDTO> page = companyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /companies/count} : count all the companies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/companies/count")
    public ResponseEntity<Long> countCompanies(CompanyCriteria criteria) {
        log.debug("REST request to count Companies by criteria: {}", criteria);
        return ResponseEntity.ok().body(companyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /companies/:id} : get the "id" company.
     *
     * @param id the id of the companyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable UUID id) {
        log.debug("REST request to get Company : {}", id);
        Optional<CompanyDTO> companyDTO = companyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyDTO);
    }

    /**
     * {@code DELETE  /companies/:id} : delete the "id" company.
     *
     * @param id the id of the companyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        log.debug("REST request to delete Company : {}", id);
        //companyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
