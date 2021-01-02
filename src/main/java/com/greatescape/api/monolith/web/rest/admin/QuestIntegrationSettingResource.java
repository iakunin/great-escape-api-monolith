package com.greatescape.api.monolith.web.rest.admin;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.service.QuestIntegrationSettingQueryService;
import com.greatescape.api.monolith.service.QuestIntegrationSettingService;
import com.greatescape.api.monolith.service.dto.QuestIntegrationSettingCriteria;
import com.greatescape.api.monolith.service.dto.QuestIntegrationSettingDTO;
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
 * REST controller for managing {@link QuestIntegrationSetting}.
 */
@RestController
@RequestMapping("/admin-api")
@RequiredArgsConstructor
@Slf4j
public class QuestIntegrationSettingResource {

    private static final String ENTITY_NAME = "questIntegrationSetting";

    @Value("${jhipster.clientApp.name}")
    private final String applicationName;

    private final QuestIntegrationSettingService questIntegrationSettingService;

    private final QuestIntegrationSettingQueryService questIntegrationSettingQueryService;

    /**
     * {@code POST  /quest-integration-settings} : Create a new questIntegrationSetting.
     *
     * @param questIntegrationSettingDTO the questIntegrationSettingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questIntegrationSettingDTO, or with status {@code 400 (Bad Request)} if the questIntegrationSetting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quest-integration-settings")
    public ResponseEntity<QuestIntegrationSettingDTO> createQuestIntegrationSetting(@Valid @RequestBody QuestIntegrationSettingDTO questIntegrationSettingDTO) throws URISyntaxException {
        log.debug("REST request to save QuestIntegrationSetting : {}", questIntegrationSettingDTO);
        if (questIntegrationSettingDTO.getId() != null) {
            throw new BadRequestAlertException("A new questIntegrationSetting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestIntegrationSettingDTO result = questIntegrationSettingService.save(questIntegrationSettingDTO);
        return ResponseEntity.created(new URI("/admin-api/quest-integration-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quest-integration-settings} : Updates an existing questIntegrationSetting.
     *
     * @param questIntegrationSettingDTO the questIntegrationSettingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questIntegrationSettingDTO,
     * or with status {@code 400 (Bad Request)} if the questIntegrationSettingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questIntegrationSettingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quest-integration-settings")
    public ResponseEntity<QuestIntegrationSettingDTO> updateQuestIntegrationSetting(@Valid @RequestBody QuestIntegrationSettingDTO questIntegrationSettingDTO) throws URISyntaxException {
        log.debug("REST request to update QuestIntegrationSetting : {}", questIntegrationSettingDTO);
        if (questIntegrationSettingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuestIntegrationSettingDTO result = questIntegrationSettingService.save(questIntegrationSettingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questIntegrationSettingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /quest-integration-settings} : get all the questIntegrationSettings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questIntegrationSettings in body.
     */
    @GetMapping("/quest-integration-settings")
    public ResponseEntity<List<QuestIntegrationSettingDTO>> getAllQuestIntegrationSettings(QuestIntegrationSettingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get QuestIntegrationSettings by criteria: {}", criteria);
        Page<QuestIntegrationSettingDTO> page = questIntegrationSettingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quest-integration-settings/count} : count all the questIntegrationSettings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/quest-integration-settings/count")
    public ResponseEntity<Long> countQuestIntegrationSettings(QuestIntegrationSettingCriteria criteria) {
        log.debug("REST request to count QuestIntegrationSettings by criteria: {}", criteria);
        return ResponseEntity.ok().body(questIntegrationSettingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quest-integration-settings/:id} : get the "id" questIntegrationSetting.
     *
     * @param id the id of the questIntegrationSettingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questIntegrationSettingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quest-integration-settings/{id}")
    public ResponseEntity<QuestIntegrationSettingDTO> getQuestIntegrationSetting(@PathVariable UUID id) {
        log.debug("REST request to get QuestIntegrationSetting : {}", id);
        Optional<QuestIntegrationSettingDTO> questIntegrationSettingDTO = questIntegrationSettingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questIntegrationSettingDTO);
    }

    /**
     * {@code DELETE  /quest-integration-settings/:id} : delete the "id" questIntegrationSetting.
     *
     * @param id the id of the questIntegrationSettingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quest-integration-settings/{id}")
    public ResponseEntity<Void> deleteQuestIntegrationSetting(@PathVariable UUID id) {
        log.debug("REST request to delete QuestIntegrationSetting : {}", id);
        //questIntegrationSettingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
