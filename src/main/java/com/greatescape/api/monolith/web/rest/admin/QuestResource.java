package com.greatescape.api.monolith.web.rest.admin;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.service.QuestQueryService;
import com.greatescape.api.monolith.service.QuestService;
import com.greatescape.api.monolith.service.dto.QuestCriteria;
import com.greatescape.api.monolith.service.dto.QuestDTO;
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
 * REST controller for managing {@link Quest}.
 */
@RestController("admin.QuestResource")
@RequestMapping("/admin-api")
@RequiredArgsConstructor
@Slf4j
public class QuestResource {

    private static final String ENTITY_NAME = "quest";

    @Value("${jhipster.clientApp.name}")
    private final String applicationName;

    private final QuestService questService;

    private final QuestQueryService questQueryService;

    /**
     * {@code POST  /quests} : Create a new quest.
     *
     * @param questDTO the questDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questDTO, or with status {@code 400 (Bad Request)} if the quest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quests")
    public ResponseEntity<QuestDTO> createQuest(@Valid @RequestBody QuestDTO questDTO) throws URISyntaxException {
        log.debug("REST request to save Quest : {}", questDTO);
        if (questDTO.getId() != null) {
            throw new BadRequestAlertException("A new quest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestDTO result = questService.save(questDTO);
        return ResponseEntity.created(new URI("/admin-api/quests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quests} : Updates an existing quest.
     *
     * @param questDTO the questDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questDTO,
     * or with status {@code 400 (Bad Request)} if the questDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quests")
    public ResponseEntity<QuestDTO> updateQuest(@Valid @RequestBody QuestDTO questDTO) throws URISyntaxException {
        log.debug("REST request to update Quest : {}", questDTO);
        if (questDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuestDTO result = questService.save(questDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /quests} : get all the quests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quests in body.
     */
    @GetMapping("/quests")
    public ResponseEntity<List<QuestDTO>> getAllQuests(QuestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Quests by criteria: {}", criteria);
        Page<QuestDTO> page = questQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quests/count} : count all the quests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/quests/count")
    public ResponseEntity<Long> countQuests(QuestCriteria criteria) {
        log.debug("REST request to count Quests by criteria: {}", criteria);
        return ResponseEntity.ok().body(questQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quests/:id} : get the "id" quest.
     *
     * @param id the id of the questDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quests/{id}")
    public ResponseEntity<QuestDTO> getQuest(@PathVariable UUID id) {
        log.debug("REST request to get Quest : {}", id);
        Optional<QuestDTO> questDTO = questService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questDTO);
    }

    /**
     * {@code DELETE  /quests/:id} : delete the "id" quest.
     *
     * @param id the id of the questDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quests/{id}")
    public ResponseEntity<Void> deleteQuest(@PathVariable UUID id) {
        log.debug("REST request to delete Quest : {}", id);
        //questService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
