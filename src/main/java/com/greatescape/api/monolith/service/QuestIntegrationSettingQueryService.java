package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.QuestIntegrationSetting_;
import com.greatescape.api.monolith.domain.Quest_;
import com.greatescape.api.monolith.repository.QuestIntegrationSettingRepository;
import com.greatescape.api.monolith.service.dto.QuestIntegrationSettingCriteria;
import com.greatescape.api.monolith.service.dto.QuestIntegrationSettingDTO;
import com.greatescape.api.monolith.service.mapper.QuestIntegrationSettingMapper;
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
 * Service for executing complex queries for {@link QuestIntegrationSetting} entities in the database.
 * The main input is a {@link QuestIntegrationSettingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestIntegrationSettingDTO} or a {@link Page} of {@link QuestIntegrationSettingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestIntegrationSettingQueryService extends QueryService<QuestIntegrationSetting> {

    private final Logger log = LoggerFactory.getLogger(QuestIntegrationSettingQueryService.class);

    private final QuestIntegrationSettingRepository questIntegrationSettingRepository;

    private final QuestIntegrationSettingMapper questIntegrationSettingMapper;

    public QuestIntegrationSettingQueryService(QuestIntegrationSettingRepository questIntegrationSettingRepository, QuestIntegrationSettingMapper questIntegrationSettingMapper) {
        this.questIntegrationSettingRepository = questIntegrationSettingRepository;
        this.questIntegrationSettingMapper = questIntegrationSettingMapper;
    }

    /**
     * Return a {@link List} of {@link QuestIntegrationSettingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuestIntegrationSettingDTO> findByCriteria(QuestIntegrationSettingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<QuestIntegrationSetting> specification = createSpecification(criteria);
        return questIntegrationSettingMapper.toDto(questIntegrationSettingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuestIntegrationSettingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestIntegrationSettingDTO> findByCriteria(QuestIntegrationSettingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuestIntegrationSetting> specification = createSpecification(criteria);
        return questIntegrationSettingRepository.findAll(specification, page)
            .map(questIntegrationSettingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestIntegrationSettingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QuestIntegrationSetting> specification = createSpecification(criteria);
        return questIntegrationSettingRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestIntegrationSettingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuestIntegrationSetting> createSpecification(QuestIntegrationSettingCriteria criteria) {
        Specification<QuestIntegrationSetting> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), QuestIntegrationSetting_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), QuestIntegrationSetting_.type));
            }
            if (criteria.getQuestId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestId(),
                    root -> root.join(QuestIntegrationSetting_.quest, JoinType.LEFT).get(Quest_.id)));
            }
        }
        return specification;
    }
}
