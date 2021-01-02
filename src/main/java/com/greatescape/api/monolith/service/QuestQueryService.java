package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Company_;
import com.greatescape.api.monolith.domain.Location_;
import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.Quest_;
import com.greatescape.api.monolith.domain.Thematic_;
import com.greatescape.api.monolith.repository.QuestRepository;
import com.greatescape.api.monolith.service.dto.QuestCriteria;
import com.greatescape.api.monolith.service.dto.QuestDTO;
import com.greatescape.api.monolith.service.mapper.QuestMapper;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link Quest} entities in the database.
 * The main input is a {@link QuestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestDTO} or a {@link Page} of {@link QuestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class QuestQueryService extends QueryService<Quest> {

    private final QuestRepository questRepository;

    private final QuestMapper questMapper;

    /**
     * Return a {@link List} of {@link QuestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuestDTO> findByCriteria(QuestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Quest> specification = createSpecification(criteria);
        return questMapper.toDto(questRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestDTO> findByCriteria(QuestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Quest> specification = createSpecification(criteria);
        return questRepository.findAll(specification, page)
            .map(questMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Quest> specification = createSpecification(criteria);
        return questRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Quest> createSpecification(QuestCriteria criteria) {
        Specification<Quest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Quest_.id));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Quest_.slug));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Quest_.title));
            }
            if (criteria.getPlayersMinCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayersMinCount(), Quest_.playersMinCount));
            }
            if (criteria.getPlayersMaxCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayersMaxCount(), Quest_.playersMaxCount));
            }
            if (criteria.getDurationInMinutes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDurationInMinutes(), Quest_.durationInMinutes));
            }
            if (criteria.getComplexity() != null) {
                specification = specification.and(buildSpecification(criteria.getComplexity(), Quest_.complexity));
            }
            if (criteria.getFearLevel() != null) {
                specification = specification.and(buildSpecification(criteria.getFearLevel(), Quest_.fearLevel));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Quest_.type));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Quest_.location, JoinType.LEFT).get(Location_.id)));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildSpecification(criteria.getCompanyId(),
                    root -> root.join(Quest_.company, JoinType.LEFT).get(Company_.id)));
            }
            if (criteria.getThematicId() != null) {
                specification = specification.and(buildSpecification(criteria.getThematicId(),
                    root -> root.join(Quest_.thematics, JoinType.LEFT).get(Thematic_.id)));
            }
        }
        return specification;
    }
}
