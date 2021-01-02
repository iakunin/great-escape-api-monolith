package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.Company;
import com.greatescape.api.monolith.domain.Company_;
import com.greatescape.api.monolith.repository.CompanyRepository;
import com.greatescape.api.monolith.service.dto.CompanyCriteria;
import com.greatescape.api.monolith.service.dto.CompanyDTO;
import com.greatescape.api.monolith.service.mapper.CompanyMapper;
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
 * Service for executing complex queries for {@link Company} entities in the database.
 * The main input is a {@link CompanyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompanyDTO} or a {@link Page} of {@link CompanyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CompanyQueryService extends QueryService<Company> {

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    /**
     * Return a {@link List} of {@link CompanyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompanyDTO> findByCriteria(CompanyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Company> specification = createSpecification(criteria);
        return companyMapper.toDto(companyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CompanyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyDTO> findByCriteria(CompanyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Company> specification = createSpecification(criteria);
        return companyRepository.findAll(specification, page)
            .map(companyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompanyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Company> specification = createSpecification(criteria);
        return companyRepository.count(specification);
    }

    /**
     * Function to convert {@link CompanyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Company> createSpecification(CompanyCriteria criteria) {
        Specification<Company> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Company_.id));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Company_.slug));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Company_.title));
            }
            if (criteria.getLegalName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLegalName(), Company_.legalName));
            }
            if (criteria.getTaxpayerNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxpayerNumber(), Company_.taxpayerNumber));
            }
            if (criteria.getDiscountInPercents() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountInPercents(), Company_.discountInPercents));
            }
            if (criteria.getCommissionInPercents() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommissionInPercents(), Company_.commissionInPercents));
            }
        }
        return specification;
    }
}
