package com.greatescape.api.monolith.web.rest.admin;

import com.greatescape.api.monolith.domain.Location;
import com.greatescape.api.monolith.service.LocationQueryService;
import com.greatescape.api.monolith.service.LocationService;
import com.greatescape.api.monolith.service.dto.LocationCriteria;
import com.greatescape.api.monolith.service.dto.LocationDTO;
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
 * REST controller for managing {@link Location}.
 */
@RestController
@RequestMapping("/admin-api")
@RequiredArgsConstructor
@Slf4j
public class LocationResource {

    private static final String ENTITY_NAME = "location";

    @Value("${jhipster.clientApp.name}")
    private final String applicationName;

    private final LocationService locationService;

    private final LocationQueryService locationQueryService;

    /**
     * {@code POST  /locations} : Create a new location.
     *
     * @param locationDTO the locationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationDTO, or with status {@code 400 (Bad Request)} if the location has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locations")
    public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO locationDTO) throws URISyntaxException {
        log.debug("REST request to save Location : {}", locationDTO);
        if (locationDTO.getId() != null) {
            throw new BadRequestAlertException("A new location cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationDTO result = locationService.save(locationDTO);
        return ResponseEntity.created(new URI("/admin-api/locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /locations} : Updates an existing location.
     *
     * @param locationDTO the locationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationDTO,
     * or with status {@code 400 (Bad Request)} if the locationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locations")
    public ResponseEntity<LocationDTO> updateLocation(@Valid @RequestBody LocationDTO locationDTO) throws URISyntaxException {
        log.debug("REST request to update Location : {}", locationDTO);
        if (locationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LocationDTO result = locationService.save(locationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /locations} : get all the locations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locations in body.
     */
    @GetMapping("/locations")
    public ResponseEntity<List<LocationDTO>> getAllLocations(LocationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Locations by criteria: {}", criteria);
        Page<LocationDTO> page = locationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /locations/count} : count all the locations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/locations/count")
    public ResponseEntity<Long> countLocations(LocationCriteria criteria) {
        log.debug("REST request to count Locations by criteria: {}", criteria);
        return ResponseEntity.ok().body(locationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /locations/:id} : get the "id" location.
     *
     * @param id the id of the locationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable UUID id) {
        log.debug("REST request to get Location : {}", id);
        Optional<LocationDTO> locationDTO = locationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationDTO);
    }

    /**
     * {@code DELETE  /locations/:id} : delete the "id" location.
     *
     * @param id the id of the locationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable UUID id) {
        log.debug("REST request to delete Location : {}", id);
        //locationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
