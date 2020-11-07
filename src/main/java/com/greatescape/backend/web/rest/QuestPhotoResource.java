package com.greatescape.backend.web.rest;

import com.greatescape.backend.service.QuestPhotoQueryService;
import com.greatescape.backend.service.QuestPhotoService;
import com.greatescape.backend.service.dto.QuestPhotoCriteria;
import com.greatescape.backend.service.dto.QuestPhotoDTO;
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
 * REST controller for managing {@link com.greatescape.backend.domain.QuestPhoto}.
 */
@RestController
@RequestMapping("/api")
public class QuestPhotoResource {

    private final Logger log = LoggerFactory.getLogger(QuestPhotoResource.class);

    private static final String ENTITY_NAME = "questPhoto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestPhotoService questPhotoService;

    private final QuestPhotoQueryService questPhotoQueryService;

    public QuestPhotoResource(QuestPhotoService questPhotoService, QuestPhotoQueryService questPhotoQueryService) {
        this.questPhotoService = questPhotoService;
        this.questPhotoQueryService = questPhotoQueryService;
    }

    /**
     * {@code POST  /quest-photos} : Create a new questPhoto.
     *
     * @param questPhotoDTO the questPhotoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questPhotoDTO, or with status {@code 400 (Bad Request)} if the questPhoto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quest-photos")
    public ResponseEntity<QuestPhotoDTO> createQuestPhoto(@Valid @RequestBody QuestPhotoDTO questPhotoDTO) throws URISyntaxException {
        log.debug("REST request to save QuestPhoto : {}", questPhotoDTO);
        if (questPhotoDTO.getId() != null) {
            throw new BadRequestAlertException("A new questPhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestPhotoDTO result = questPhotoService.save(questPhotoDTO);
        return ResponseEntity.created(new URI("/api/quest-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quest-photos} : Updates an existing questPhoto.
     *
     * @param questPhotoDTO the questPhotoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questPhotoDTO,
     * or with status {@code 400 (Bad Request)} if the questPhotoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questPhotoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quest-photos")
    public ResponseEntity<QuestPhotoDTO> updateQuestPhoto(@Valid @RequestBody QuestPhotoDTO questPhotoDTO) throws URISyntaxException {
        log.debug("REST request to update QuestPhoto : {}", questPhotoDTO);
        if (questPhotoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuestPhotoDTO result = questPhotoService.save(questPhotoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questPhotoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /quest-photos} : get all the questPhotos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questPhotos in body.
     */
    @GetMapping("/quest-photos")
    public ResponseEntity<List<QuestPhotoDTO>> getAllQuestPhotos(QuestPhotoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get QuestPhotos by criteria: {}", criteria);
        Page<QuestPhotoDTO> page = questPhotoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quest-photos/count} : count all the questPhotos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/quest-photos/count")
    public ResponseEntity<Long> countQuestPhotos(QuestPhotoCriteria criteria) {
        log.debug("REST request to count QuestPhotos by criteria: {}", criteria);
        return ResponseEntity.ok().body(questPhotoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quest-photos/:id} : get the "id" questPhoto.
     *
     * @param id the id of the questPhotoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questPhotoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quest-photos/{id}")
    public ResponseEntity<QuestPhotoDTO> getQuestPhoto(@PathVariable Long id) {
        log.debug("REST request to get QuestPhoto : {}", id);
        Optional<QuestPhotoDTO> questPhotoDTO = questPhotoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questPhotoDTO);
    }

    /**
     * {@code DELETE  /quest-photos/:id} : delete the "id" questPhoto.
     *
     * @param id the id of the questPhotoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quest-photos/{id}")
    public ResponseEntity<Void> deleteQuestPhoto(@PathVariable Long id) {
        log.debug("REST request to delete QuestPhoto : {}", id);
        questPhotoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
