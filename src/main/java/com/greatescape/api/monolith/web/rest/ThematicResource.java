package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.service.ThematicQueryService;
import com.greatescape.api.monolith.service.ThematicService;
import com.greatescape.api.monolith.service.dto.ThematicCriteria;
import com.greatescape.api.monolith.service.dto.ThematicDTO;
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
 * REST controller for managing {@link Thematic}.
 */
@RestController
@RequestMapping("/api")
public class ThematicResource {

    private final Logger log = LoggerFactory.getLogger(ThematicResource.class);

    private static final String ENTITY_NAME = "thematic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThematicService thematicService;

    private final ThematicQueryService thematicQueryService;

    public ThematicResource(ThematicService thematicService, ThematicQueryService thematicQueryService) {
        this.thematicService = thematicService;
        this.thematicQueryService = thematicQueryService;
    }

    /**
     * {@code POST  /thematics} : Create a new thematic.
     *
     * @param thematicDTO the thematicDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new thematicDTO, or with status {@code 400 (Bad Request)} if the thematic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/thematics")
    public ResponseEntity<ThematicDTO> createThematic(@Valid @RequestBody ThematicDTO thematicDTO) throws URISyntaxException {
        log.debug("REST request to save Thematic : {}", thematicDTO);
        if (thematicDTO.getId() != null) {
            throw new BadRequestAlertException("A new thematic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ThematicDTO result = thematicService.save(thematicDTO);
        return ResponseEntity.created(new URI("/api/thematics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /thematics} : Updates an existing thematic.
     *
     * @param thematicDTO the thematicDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated thematicDTO,
     * or with status {@code 400 (Bad Request)} if the thematicDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the thematicDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/thematics")
    public ResponseEntity<ThematicDTO> updateThematic(@Valid @RequestBody ThematicDTO thematicDTO) throws URISyntaxException {
        log.debug("REST request to update Thematic : {}", thematicDTO);
        if (thematicDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ThematicDTO result = thematicService.save(thematicDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, thematicDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /thematics} : get all the thematics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of thematics in body.
     */
    @GetMapping("/thematics")
    public ResponseEntity<List<ThematicDTO>> getAllThematics(ThematicCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Thematics by criteria: {}", criteria);
        Page<ThematicDTO> page = thematicQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /thematics/count} : count all the thematics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/thematics/count")
    public ResponseEntity<Long> countThematics(ThematicCriteria criteria) {
        log.debug("REST request to count Thematics by criteria: {}", criteria);
        return ResponseEntity.ok().body(thematicQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /thematics/:id} : get the "id" thematic.
     *
     * @param id the id of the thematicDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the thematicDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/thematics/{id}")
    public ResponseEntity<ThematicDTO> getThematic(@PathVariable UUID id) {
        log.debug("REST request to get Thematic : {}", id);
        Optional<ThematicDTO> thematicDTO = thematicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(thematicDTO);
    }

    /**
     * {@code DELETE  /thematics/:id} : delete the "id" thematic.
     *
     * @param id the id of the thematicDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/thematics/{id}")
    public ResponseEntity<Void> deleteThematic(@PathVariable UUID id) {
        log.debug("REST request to delete Thematic : {}", id);
        thematicService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
