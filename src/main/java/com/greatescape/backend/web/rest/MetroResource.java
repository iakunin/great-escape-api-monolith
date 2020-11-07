package com.greatescape.backend.web.rest;

import com.greatescape.backend.service.MetroQueryService;
import com.greatescape.backend.service.MetroService;
import com.greatescape.backend.service.dto.MetroCriteria;
import com.greatescape.backend.service.dto.MetroDTO;
import com.greatescape.backend.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link com.greatescape.backend.domain.Metro}.
 */
@RestController
@RequestMapping("/api")
public class MetroResource {

    private final Logger log = LoggerFactory.getLogger(MetroResource.class);

    private static final String ENTITY_NAME = "metro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetroService metroService;

    private final MetroQueryService metroQueryService;

    public MetroResource(MetroService metroService, MetroQueryService metroQueryService) {
        this.metroService = metroService;
        this.metroQueryService = metroQueryService;
    }

    /**
     * {@code POST  /metros} : Create a new metro.
     *
     * @param metroDTO the metroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metroDTO, or with status {@code 400 (Bad Request)} if the metro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/metros")
    public ResponseEntity<MetroDTO> createMetro(@Valid @RequestBody MetroDTO metroDTO) throws URISyntaxException {
        log.debug("REST request to save Metro : {}", metroDTO);
        if (metroDTO.getId() != null) {
            throw new BadRequestAlertException("A new metro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MetroDTO result = metroService.save(metroDTO);
        return ResponseEntity.created(new URI("/api/metros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /metros} : Updates an existing metro.
     *
     * @param metroDTO the metroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metroDTO,
     * or with status {@code 400 (Bad Request)} if the metroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/metros")
    public ResponseEntity<MetroDTO> updateMetro(@Valid @RequestBody MetroDTO metroDTO) throws URISyntaxException {
        log.debug("REST request to update Metro : {}", metroDTO);
        if (metroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MetroDTO result = metroService.save(metroDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metroDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /metros} : get all the metros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metros in body.
     */
    @GetMapping("/metros")
    public ResponseEntity<List<MetroDTO>> getAllMetros(MetroCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Metros by criteria: {}", criteria);
        Page<MetroDTO> page = metroQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /metros/count} : count all the metros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/metros/count")
    public ResponseEntity<Long> countMetros(MetroCriteria criteria) {
        log.debug("REST request to count Metros by criteria: {}", criteria);
        return ResponseEntity.ok().body(metroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /metros/:id} : get the "id" metro.
     *
     * @param id the id of the metroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/metros/{id}")
    public ResponseEntity<MetroDTO> getMetro(@PathVariable Long id) {
        log.debug("REST request to get Metro : {}", id);
        Optional<MetroDTO> metroDTO = metroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metroDTO);
    }

    /**
     * {@code DELETE  /metros/:id} : delete the "id" metro.
     *
     * @param id the id of the metroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/metros/{id}")
    public ResponseEntity<Void> deleteMetro(@PathVariable Long id) {
        log.debug("REST request to delete Metro : {}", id);
        metroService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
