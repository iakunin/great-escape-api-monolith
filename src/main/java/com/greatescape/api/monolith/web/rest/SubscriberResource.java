package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.domain.Subscriber;
import com.greatescape.api.monolith.service.SubscriberQueryService;
import com.greatescape.api.monolith.service.SubscriberService;
import com.greatescape.api.monolith.service.dto.SubscriberCriteria;
import com.greatescape.api.monolith.service.dto.SubscriberDTO;
import com.greatescape.api.monolith.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link Subscriber}.
 */
@RestController
@RequestMapping("/api")
public class SubscriberResource {

    private final Logger log = LoggerFactory.getLogger(SubscriberResource.class);

    private static final String ENTITY_NAME = "subscriber";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriberService subscriberService;

    private final SubscriberQueryService subscriberQueryService;

    public SubscriberResource(SubscriberService subscriberService, SubscriberQueryService subscriberQueryService) {
        this.subscriberService = subscriberService;
        this.subscriberQueryService = subscriberQueryService;
    }

    /**
     * {@code POST  /subscribers} : Create a new subscriber.
     *
     * @param subscriberDTO the subscriberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriberDTO, or with status {@code 400 (Bad Request)} if the subscriber has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscribers")
    public ResponseEntity<SubscriberDTO> createSubscriber(@Valid @RequestBody SubscriberDTO subscriberDTO) throws URISyntaxException {
        log.debug("REST request to save Subscriber : {}", subscriberDTO);
        if (subscriberDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriber cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriberDTO result = subscriberService.save(subscriberDTO);
        return ResponseEntity.created(new URI("/api/subscribers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscribers} : Updates an existing subscriber.
     *
     * @param subscriberDTO the subscriberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriberDTO,
     * or with status {@code 400 (Bad Request)} if the subscriberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscribers")
    public ResponseEntity<SubscriberDTO> updateSubscriber(@Valid @RequestBody SubscriberDTO subscriberDTO) throws URISyntaxException {
        log.debug("REST request to update Subscriber : {}", subscriberDTO);
        if (subscriberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubscriberDTO result = subscriberService.save(subscriberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriberDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subscribers} : get all the subscribers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscribers in body.
     */
    @GetMapping("/subscribers")
    public ResponseEntity<List<SubscriberDTO>> getAllSubscribers(SubscriberCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Subscribers by criteria: {}", criteria);
        Page<SubscriberDTO> page = subscriberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscribers/count} : count all the subscribers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/subscribers/count")
    public ResponseEntity<Long> countSubscribers(SubscriberCriteria criteria) {
        log.debug("REST request to count Subscribers by criteria: {}", criteria);
        return ResponseEntity.ok().body(subscriberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /subscribers/:id} : get the "id" subscriber.
     *
     * @param id the id of the subscriberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscribers/{id}")
    public ResponseEntity<SubscriberDTO> getSubscriber(@PathVariable Long id) {
        log.debug("REST request to get Subscriber : {}", id);
        Optional<SubscriberDTO> subscriberDTO = subscriberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriberDTO);
    }

    /**
     * {@code DELETE  /subscribers/:id} : delete the "id" subscriber.
     *
     * @param id the id of the subscriberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscribers/{id}")
    public ResponseEntity<Void> deleteSubscriber(@PathVariable Long id) {
        log.debug("REST request to delete Subscriber : {}", id);
        subscriberService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
