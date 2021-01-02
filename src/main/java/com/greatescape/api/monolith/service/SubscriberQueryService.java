package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Subscriber;
import com.greatescape.api.monolith.domain.Subscriber_;
import com.greatescape.api.monolith.repository.SubscriberRepository;
import com.greatescape.api.monolith.service.dto.SubscriberCriteria;
import com.greatescape.api.monolith.service.dto.SubscriberDTO;
import com.greatescape.api.monolith.service.mapper.SubscriberMapper;
import io.github.jhipster.service.QueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link Subscriber} entities in the database.
 * The main input is a {@link SubscriberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubscriberDTO} or a {@link Page} of {@link SubscriberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SubscriberQueryService extends QueryService<Subscriber> {

    private final SubscriberRepository subscriberRepository;

    private final SubscriberMapper subscriberMapper;

    /**
     * Return a {@link List} of {@link SubscriberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriberDTO> findByCriteria(SubscriberCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Subscriber> specification = createSpecification(criteria);
        return subscriberMapper.toDto(subscriberRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubscriberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscriberDTO> findByCriteria(SubscriberCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Subscriber> specification = createSpecification(criteria);
        return subscriberRepository.findAll(specification, page)
            .map(subscriberMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubscriberCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Subscriber> specification = createSpecification(criteria);
        return subscriberRepository.count(specification);
    }

    /**
     * Function to convert {@link SubscriberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Subscriber> createSpecification(SubscriberCriteria criteria) {
        Specification<Subscriber> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Subscriber_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Subscriber_.name));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Subscriber_.email));
            }
        }
        return specification;
    }
}
