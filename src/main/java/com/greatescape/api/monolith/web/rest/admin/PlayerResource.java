package com.greatescape.api.monolith.web.rest.admin;

import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.service.PlayerQueryService;
import com.greatescape.api.monolith.service.PlayerService;
import com.greatescape.api.monolith.service.dto.PlayerCriteria;
import com.greatescape.api.monolith.service.dto.PlayerDTO;
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
 * REST controller for managing {@link Player}.
 */
@RestController
@RequestMapping("/admin-api")
@RequiredArgsConstructor
@Slf4j
public class PlayerResource {

    private static final String ENTITY_NAME = "player";

    @Value("${jhipster.clientApp.name}")
    private final String applicationName;

    private final PlayerService playerService;

    private final PlayerQueryService playerQueryService;

    /**
     * {@code POST  /players} : Create a new player.
     *
     * @param playerDTO the playerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerDTO, or with status {@code 400 (Bad Request)} if the player has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/players")
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO playerDTO) throws URISyntaxException {
        log.debug("REST request to save Player : {}", playerDTO);
        if (playerDTO.getId() != null) {
            throw new BadRequestAlertException("A new player cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerDTO result = playerService.save(playerDTO);
        return ResponseEntity.created(new URI("/admin-api/players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /players} : Updates an existing player.
     *
     * @param playerDTO the playerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerDTO,
     * or with status {@code 400 (Bad Request)} if the playerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/players")
    public ResponseEntity<PlayerDTO> updatePlayer(@Valid @RequestBody PlayerDTO playerDTO) throws URISyntaxException {
        log.debug("REST request to update Player : {}", playerDTO);
        if (playerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlayerDTO result = playerService.save(playerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /players} : get all the players.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of players in body.
     */
    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(PlayerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Players by criteria: {}", criteria);
        Page<PlayerDTO> page = playerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /players/count} : count all the players.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/players/count")
    public ResponseEntity<Long> countPlayers(PlayerCriteria criteria) {
        log.debug("REST request to count Players by criteria: {}", criteria);
        return ResponseEntity.ok().body(playerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /players/:id} : get the "id" player.
     *
     * @param id the id of the playerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/players/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable UUID id) {
        log.debug("REST request to get Player : {}", id);
        Optional<PlayerDTO> playerDTO = playerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playerDTO);
    }

    /**
     * {@code DELETE  /players/:id} : delete the "id" player.
     *
     * @param id the id of the playerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable UUID id) {
        log.debug("REST request to delete Player : {}", id);
        //playerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
