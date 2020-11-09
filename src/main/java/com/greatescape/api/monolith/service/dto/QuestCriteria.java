package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.enumeration.FearLevel;
import com.greatescape.api.monolith.domain.enumeration.QuestComplexity;
import com.greatescape.api.monolith.domain.enumeration.QuestType;
import com.greatescape.api.monolith.web.rest.QuestResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link Quest} entity. This class is used
 * in {@link QuestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public final class QuestCriteria implements Serializable, Criteria {

    /**
     * Class for filtering QuestComplexity
     */
    @NoArgsConstructor
    public static class QuestComplexityFilter extends Filter<QuestComplexity> {

        public QuestComplexityFilter(QuestComplexityFilter filter) {
            super(filter);
        }

        @Override
        public QuestComplexityFilter copy() {
            return new QuestComplexityFilter(this);
        }

    }

    /**
     * Class for filtering FearLevel
     */
    @NoArgsConstructor
    public static class FearLevelFilter extends Filter<FearLevel> {

        public FearLevelFilter(FearLevelFilter filter) {
            super(filter);
        }

        @Override
        public FearLevelFilter copy() {
            return new FearLevelFilter(this);
        }

    }

    /**
     * Class for filtering QuestType
     */
    @NoArgsConstructor
    public static class QuestTypeFilter extends Filter<QuestType> {

        public QuestTypeFilter(QuestTypeFilter filter) {
            super(filter);
        }

        @Override
        public QuestTypeFilter copy() {
            return new QuestTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter slug;

    private StringFilter title;

    private IntegerFilter playersMinCount;

    private IntegerFilter playersMaxCount;

    private IntegerFilter durationInMinutes;

    private QuestComplexityFilter complexity;

    private FearLevelFilter fearLevel;

    private QuestTypeFilter type;

    private UUIDFilter locationId;

    private UUIDFilter companyId;

    private UUIDFilter thematicId;

    public QuestCriteria(QuestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.slug = other.slug == null ? null : other.slug.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.playersMinCount = other.playersMinCount == null ? null : other.playersMinCount.copy();
        this.playersMaxCount = other.playersMaxCount == null ? null : other.playersMaxCount.copy();
        this.durationInMinutes = other.durationInMinutes == null ? null : other.durationInMinutes.copy();
        this.complexity = other.complexity == null ? null : other.complexity.copy();
        this.fearLevel = other.fearLevel == null ? null : other.fearLevel.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.thematicId = other.thematicId == null ? null : other.thematicId.copy();
    }

    @Override
    public QuestCriteria copy() {
        return new QuestCriteria(this);
    }
}
