package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Location_;
import com.greatescape.api.monolith.domain.Metro;
import com.greatescape.api.monolith.domain.Metro_;
import com.greatescape.api.monolith.repository.MetroRepository;
import com.greatescape.api.monolith.service.dto.MetroCriteria;
import com.greatescape.api.monolith.service.dto.MetroDTO;
import com.greatescape.api.monolith.service.mapper.MetroMapper;
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
 * Service for executing complex queries for {@link Metro} entities in the database.
 * The main input is a {@link MetroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MetroDTO} or a {@link Page} of {@link MetroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetroQueryService extends QueryService<Metro> {

    private final Logger log = LoggerFactory.getLogger(MetroQueryService.class);

    private final MetroRepository metroRepository;

    private final MetroMapper metroMapper;

    public MetroQueryService(MetroRepository metroRepository, MetroMapper metroMapper) {
        this.metroRepository = metroRepository;
        this.metroMapper = metroMapper;
    }

    /**
     * Return a {@link List} of {@link MetroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MetroDTO> findByCriteria(MetroCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Metro> specification = createSpecification(criteria);
        return metroMapper.toDto(metroRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MetroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetroDTO> findByCriteria(MetroCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Metro> specification = createSpecification(criteria);
        return metroRepository.findAll(specification, page)
            .map(metroMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetroCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Metro> specification = createSpecification(criteria);
        return metroRepository.count(specification);
    }

    /**
     * Function to convert {@link MetroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Metro> createSpecification(MetroCriteria criteria) {
        Specification<Metro> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Metro_.id));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Metro_.slug));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Metro_.title));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocationId(),
                    root -> root.join(Metro_.locations, JoinType.LEFT).get(Location_.id)));
            }
        }
        return specification;
    }
}
