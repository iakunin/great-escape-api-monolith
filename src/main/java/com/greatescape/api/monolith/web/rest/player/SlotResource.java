package com.greatescape.api.monolith.web.rest.player;

import com.greatescape.api.monolith.domain.Quest_;
import com.greatescape.api.monolith.domain.Slot;
import com.greatescape.api.monolith.domain.Slot_;
import com.greatescape.api.monolith.repository.SlotRepository;
import com.greatescape.api.monolith.service.dto.SlotCriteria;
import com.greatescape.api.monolith.service.dto.player.SlotDTO;
import com.greatescape.api.monolith.service.mapper.player.SlotMapper;
import io.github.jhipster.service.QueryService;
import io.github.jhipster.web.util.PaginationUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.persistence.criteria.JoinType;
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
 * REST controller for managing {@link Slot}.
 */
@RestController("player.SlotResource")
@RequestMapping("/player-api")
@RequiredArgsConstructor
@Slf4j
public class SlotResource extends QueryService<Slot> {

    private final SlotRepository slotRepository;

    private final SlotMapper slotMapper;

    /**
     * {@code GET  /slots} : get all the slots.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of slots in body.
     */
    @GetMapping("/slots")
    public ResponseEntity<List<SlotDTO>> getAllSlots(SlotCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Slots by criteria: {}", criteria);
        final Page<SlotDTO> page = slotRepository
            .findAll(createSpecification(criteria), pageable)
            .map(slotMapper::toDto)
            .map(slot -> {
                final int absoluteDiscount = new BigDecimal(
                    slot.getPriceWithoutDiscount() * slot.getDiscountInPercents()
                )
                    .divide(new BigDecimal(100), 0, RoundingMode.CEILING)
                    .intValueExact();

                return slot.setPriceWithDiscount(slot.getPriceWithoutDiscount() - absoluteDiscount);
            });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Function to convert {@link SlotCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    private Specification<Slot> createSpecification(SlotCriteria criteria) {
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
