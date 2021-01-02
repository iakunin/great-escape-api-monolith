package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Booking;
import com.greatescape.api.monolith.domain.Booking_;
import com.greatescape.api.monolith.domain.Player_;
import com.greatescape.api.monolith.domain.Quest_;
import com.greatescape.api.monolith.domain.Slot_;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.service.dto.BookingCriteria;
import com.greatescape.api.monolith.service.dto.BookingDTO;
import com.greatescape.api.monolith.service.mapper.BookingMapper;
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
 * Service for executing complex queries for {@link Booking} entities in the database.
 * The main input is a {@link BookingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BookingDTO} or a {@link Page} of {@link BookingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookingQueryService extends QueryService<Booking> {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    /**
     * Return a {@link List} of {@link BookingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BookingDTO> findByCriteria(BookingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingMapper.toDto(bookingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BookingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingDTO> findByCriteria(BookingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.findAll(specification, page)
            .map(bookingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.count(specification);
    }

    /**
     * Function to convert {@link BookingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Booking> createSpecification(BookingCriteria criteria) {
        Specification<Booking> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Booking_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Booking_.status));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Booking_.price));
            }
            if (criteria.getDiscountInPercents() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountInPercents(), Booking_.discountInPercents));
            }
            if (criteria.getCommissionInPercents() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommissionInPercents(), Booking_.commissionInPercents));
            }
            if (criteria.getSlotId() != null) {
                specification = specification.and(buildSpecification(criteria.getSlotId(),
                    root -> root.join(Booking_.slot, JoinType.LEFT).get(Slot_.id)));
            }
            if (criteria.getQuestId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestId(),
                    root -> root.join(Booking_.quest, JoinType.LEFT).get(Quest_.id)));
            }
            if (criteria.getPlayerId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlayerId(),
                    root -> root.join(Booking_.player, JoinType.LEFT).get(Player_.id)));
            }
        }
        return specification;
    }
}
