package com.greatescape.backend.service;

import com.greatescape.backend.domain.Quest_;
import com.greatescape.backend.domain.Thematic;
import com.greatescape.backend.domain.Thematic_;
import com.greatescape.backend.repository.ThematicRepository;
import com.greatescape.backend.service.dto.ThematicCriteria;
import com.greatescape.backend.service.dto.ThematicDTO;
import com.greatescape.backend.service.mapper.ThematicMapper;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link Thematic} entities in the database.
 * The main input is a {@link ThematicCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ThematicDTO} or a {@link Page} of {@link ThematicDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ThematicQueryService extends QueryService<Thematic> {

    private final Logger log = LoggerFactory.getLogger(ThematicQueryService.class);

    private final ThematicRepository thematicRepository;

    private final ThematicMapper thematicMapper;

    public ThematicQueryService(ThematicRepository thematicRepository, ThematicMapper thematicMapper) {
        this.thematicRepository = thematicRepository;
        this.thematicMapper = thematicMapper;
    }

    /**
     * Return a {@link List} of {@link ThematicDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ThematicDTO> findByCriteria(ThematicCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Thematic> specification = createSpecification(criteria);
        return thematicMapper.toDto(thematicRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ThematicDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ThematicDTO> findByCriteria(ThematicCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Thematic> specification = createSpecification(criteria);
        return thematicRepository.findAll(specification, page)
            .map(thematicMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ThematicCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Thematic> specification = createSpecification(criteria);
        return thematicRepository.count(specification);
    }

    /**
     * Function to convert {@link ThematicCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Thematic> createSpecification(ThematicCriteria criteria) {
        Specification<Thematic> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Thematic_.id));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Thematic_.slug));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Thematic_.title));
            }
            if (criteria.getQuestId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestId(),
                    root -> root.join(Thematic_.quests, JoinType.LEFT).get(Quest_.id)));
            }
        }
        return specification;
    }
}
