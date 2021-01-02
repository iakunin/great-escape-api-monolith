package com.greatescape.api.monolith.service;

import com.greatescape.api.monolith.domain.QuestPhoto;
import com.greatescape.api.monolith.domain.QuestPhoto_;
import com.greatescape.api.monolith.domain.Quest_;
import com.greatescape.api.monolith.repository.QuestPhotoRepository;
import com.greatescape.api.monolith.service.dto.QuestPhotoCriteria;
import com.greatescape.api.monolith.service.dto.QuestPhotoDTO;
import com.greatescape.api.monolith.service.mapper.QuestPhotoMapper;
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
 * Service for executing complex queries for {@link QuestPhoto} entities in the database.
 * The main input is a {@link QuestPhotoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestPhotoDTO} or a {@link Page} of {@link QuestPhotoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class QuestPhotoQueryService extends QueryService<QuestPhoto> {

    private final QuestPhotoRepository questPhotoRepository;

    private final QuestPhotoMapper questPhotoMapper;

    /**
     * Return a {@link List} of {@link QuestPhotoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuestPhotoDTO> findByCriteria(QuestPhotoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<QuestPhoto> specification = createSpecification(criteria);
        return questPhotoMapper.toDto(questPhotoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuestPhotoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestPhotoDTO> findByCriteria(QuestPhotoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuestPhoto> specification = createSpecification(criteria);
        return questPhotoRepository.findAll(specification, page)
            .map(questPhotoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestPhotoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QuestPhoto> specification = createSpecification(criteria);
        return questPhotoRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestPhotoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuestPhoto> createSpecification(QuestPhotoCriteria criteria) {
        Specification<QuestPhoto> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), QuestPhoto_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), QuestPhoto_.url));
            }
            if (criteria.getQuestId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestId(),
                    root -> root.join(QuestPhoto_.quest, JoinType.LEFT).get(Quest_.id)));
            }
        }
        return specification;
    }
}
