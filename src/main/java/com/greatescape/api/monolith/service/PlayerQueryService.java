package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Company_;
import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.Player_;
import com.greatescape.api.monolith.domain.User_;
import com.greatescape.api.monolith.repository.PlayerRepository;
import com.greatescape.api.monolith.service.dto.PlayerCriteria;
import com.greatescape.api.monolith.service.dto.PlayerDTO;
import com.greatescape.api.monolith.service.mapper.PlayerMapper;
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
 * Service for executing complex queries for {@link Player} entities in the database.
 * The main input is a {@link PlayerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlayerDTO} or a {@link Page} of {@link PlayerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PlayerQueryService extends QueryService<Player> {

    private final PlayerRepository playerRepository;

    private final PlayerMapper playerMapper;

    /**
     * Return a {@link List} of {@link PlayerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlayerDTO> findByCriteria(PlayerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Player> specification = createSpecification(criteria);
        return playerMapper.toDto(playerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlayerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlayerDTO> findByCriteria(PlayerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Player> specification = createSpecification(criteria);
        return playerRepository.findAll(specification, page)
            .map(playerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlayerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Player> specification = createSpecification(criteria);
        return playerRepository.count(specification);
    }

    /**
     * Function to convert {@link PlayerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Player> createSpecification(PlayerCriteria criteria) {
        Specification<Player> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Player_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Player_.name));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Player_.phone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Player_.email));
            }
            if (criteria.getBirthday() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthday(), Player_.birthday));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Player_.gender));
            }
            if (criteria.getSubscriptionAllowed() != null) {
                specification = specification.and(buildSpecification(criteria.getSubscriptionAllowed(), Player_.subscriptionAllowed));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getInternalUserId(),
                    root -> root.join(Player_.internalUser, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildSpecification(criteria.getCompanyId(),
                    root -> root.join(Player_.company, JoinType.LEFT).get(Company_.id)));
            }
        }
        return specification;
    }
}
