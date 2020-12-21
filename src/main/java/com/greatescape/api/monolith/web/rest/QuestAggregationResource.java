package com.greatescape.api.monolith.web.rest;

import com.greatescape.api.monolith.config.ApplicationProperties;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.QuestAggregation;
import com.greatescape.api.monolith.domain.QuestAggregation_;
import com.greatescape.api.monolith.repository.QuestAggregationRepository;
import com.greatescape.api.monolith.service.dto.QuestAggregationCriteria;
import com.greatescape.api.monolith.service.dto.QuestAggregationDTO;
import com.greatescape.api.monolith.service.dto.QuestCriteria;
import com.greatescape.api.monolith.service.mapper.QuestAggregationMapper;
import io.github.jhipster.service.QueryService;
import io.github.jhipster.web.util.PaginationUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link Quest}.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class QuestAggregationResource extends QueryService<QuestAggregation> {

    private final QuestAggregationRepository repository;

    private final QuestAggregationMapper mapper;

    private final ApplicationProperties properties;

    /**
     * {@code GET  /quest_aggregations} : get all the quest_aggregations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quests in body.
     */
    @GetMapping("/quest_aggregations")
    public ResponseEntity<List<QuestAggregationDTO>> getAllQuests(QuestAggregationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Quests by criteria: {}", criteria);
        final Specification<QuestAggregation> specification = createSpecification(criteria);

        final Page<QuestAggregationDTO> page = repository
            .findAll(specification, pageable)
            .map(mapper::toDto)
            .map(dto -> dto.setDiscountInPercents(
                Optional.ofNullable(dto.getDiscountInPercents())
                    .orElse(properties.getDiscountInPercents())
                )
            );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quest_aggregations/count} : count all the quest_aggregations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/quest_aggregations/count")
    public ResponseEntity<Long> countQuests(QuestAggregationCriteria criteria) {
        log.debug("REST request to count Quests by criteria: {}", criteria);

        return ResponseEntity.ok().body(
            repository.count(createSpecification(criteria))
        );
    }


    /**
     * Function to convert {@link QuestCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuestAggregation> createSpecification(QuestAggregationCriteria criteria) {
        Specification<QuestAggregation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), QuestAggregation_.id));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), QuestAggregation_.slug));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), QuestAggregation_.title));
            }
            if (criteria.getPlayersMinCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayersMinCount(), QuestAggregation_.playersMinCount));
            }
            if (criteria.getPlayersMaxCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayersMaxCount(), QuestAggregation_.playersMaxCount));
            }
            if (criteria.getDurationInMinutes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDurationInMinutes(), QuestAggregation_.durationInMinutes));
            }
            if (criteria.getMinPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinPrice(), QuestAggregation_.minPrice));
            }
            if (criteria.getDiscountInPercents() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountInPercents(), QuestAggregation_.discountInPercents));
            }
            if (criteria.getComplexity() != null) {
                specification = specification.and(buildSpecification(criteria.getComplexity(), QuestAggregation_.complexity));
            }
            if (criteria.getFearLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getFearLevel(), QuestAggregation_.fearLevel));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), QuestAggregation_.type));
            }
        }

        return specification;
    }
}
