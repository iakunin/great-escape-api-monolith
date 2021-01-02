package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Quest_;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.Slot_;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.service.dto.SlotCriteria;
import com.greatescape.api.monolith.service.dto.SlotDTO;
import com.greatescape.api.monolith.service.mapper.SlotMapper;
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
 * Service for executing complex queries for {@link Slot} entities in the database.
 * The main input is a {@link SlotCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SlotDTO} or a {@link Page} of {@link SlotDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SlotQueryService extends QueryService<Slot> {

    private final SlotRepository slotRepository;

    private final SlotMapper slotMapper;

    /**
     * Return a {@link List} of {@link SlotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SlotDTO> findByCriteria(SlotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Slot> specification = createSpecification(criteria);
        return slotMapper.toDto(slotRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SlotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SlotDTO> findByCriteria(SlotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Slot> specification = createSpecification(criteria);
        return slotRepository.findAll(specification, page)
            .map(slotMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SlotCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Slot> specification = createSpecification(criteria);
        return slotRepository.count(specification);
    }

    /**
     * Function to convert {@link SlotCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Slot> createSpecification(SlotCriteria criteria) {
        Specification<Slot> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Slot_.id));
            }
            if (criteria.getDateTimeLocal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTimeLocal(), Slot_.dateTimeLocal));
            }
            if (criteria.getDateTimeWithTimeZone() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTimeWithTimeZone(), Slot_.dateTimeWithTimeZone));
            }
            if (criteria.getIsAvailable() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAvailable(), Slot_.isAvailable));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Slot_.price));
            }
            if (criteria.getDiscountInPercents() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountInPercents(), Slot_.discountInPercents));
            }
            if (criteria.getCommissionInPercents() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommissionInPercents(), Slot_.commissionInPercents));
            }
            if (criteria.getExternalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExternalId(), Slot_.externalId));
            }
            if (criteria.getQuestId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestId(),
                    root -> root.join(Slot_.quest, JoinType.LEFT).get(Quest_.id)));
            }
        }
        return specification;
    }
}
