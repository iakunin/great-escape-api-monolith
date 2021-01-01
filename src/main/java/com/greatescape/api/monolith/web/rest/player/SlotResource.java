package com.greatescape.api.monolith.web.rest.player;

import com.greatescape.api.monolith.domain.Quest_;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.SlotAggregation;
import com.greatescape.api.monolith.domain.SlotAggregation_;
import com.greatescape.api.monolith.repository.BookingRepository;
import com.greatescape.api.monolith.repository.SlotAggregationRepository;
import com.greatescape.api.monolith.service.dto.SlotAggregationDTO;
import com.greatescape.api.monolith.service.dto.SlotCriteria;
import com.greatescape.api.monolith.service.mapper.SlotAggregationMapper;
import io.github.jhipster.service.QueryService;
import io.github.jhipster.web.util.PaginationUtil;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link Slot}.
 */
@RestController("player.SlotResource")
@RequestMapping("/player-api")
@RequiredArgsConstructor
@Slf4j
public class SlotResource extends QueryService<SlotAggregation> {

    private final SlotAggregationRepository slotAggregationRepository;

    private final SlotAggregationMapper slotMapper;

    private final BookingRepository bookingRepository;

    /**
     * {@code GET  /slots} : get all the slots.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of slots in body.
     */
    @GetMapping("/slots")
    public ResponseEntity<List<SlotAggregationDTO>> getAllSlots(SlotCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Slots by criteria: {}", criteria);
        final Page<SlotAggregationDTO> page = slotAggregationRepository
            .findAll(createSpecification(criteria), pageable)
            .map(slot -> {
                if (slot.getIsAvailable()) {
                    final Duration delta = Duration.ofMinutes(10);
                    slot.setIsAvailable(
                        ZonedDateTime.now()
                            .isBefore(
                                slot.getDateTimeWithTimeZone().plus(delta)
                            )
                    );
                }

                return slot;
            })
            .map(slot -> {
                if (slot.getIsAvailable()) {
                    slot.setIsAvailable(
                        !bookingRepository.existsBySlotId(slot.getId())
                    );
                }

                return slot;
            })
            .map(slotMapper::toDto);

        return ResponseEntity.ok()
            .headers(
                PaginationUtil.generatePaginationHttpHeaders(
                    ServletUriComponentsBuilder.fromCurrentRequest(), page
                )
            ).body(page.getContent());
    }

    /**
     * Function to convert {@link SlotCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    private Specification<SlotAggregation> createSpecification(SlotCriteria criteria) {
        Specification<SlotAggregation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SlotAggregation_.id));
            }
            if (criteria.getDateTimeLocal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTimeLocal(), SlotAggregation_.dateTimeLocal));
            }
            if (criteria.getDateTimeWithTimeZone() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTimeWithTimeZone(), SlotAggregation_.dateTimeWithTimeZone));
            }
            if (criteria.getIsAvailable() != null) {
                specification = specification.and(buildSpecification(criteria.getIsAvailable(), SlotAggregation_.isAvailable));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), SlotAggregation_.priceWithDiscount));
            }
            if (criteria.getDiscountInPercents() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountInPercents(), SlotAggregation_.discountInPercents));
            }
            if (criteria.getCommissionInPercents() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommissionInPercents(), SlotAggregation_.commissionInPercents));
            }
            if (criteria.getExternalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExternalId(), SlotAggregation_.externalId));
            }
            if (criteria.getQuestId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestId(),
                    root -> root.join(SlotAggregation_.quest, JoinType.LEFT).get(Quest_.id)));
            }
        }
        return specification;
    }
}
