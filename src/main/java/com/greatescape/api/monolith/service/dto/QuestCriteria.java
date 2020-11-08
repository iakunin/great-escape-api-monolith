package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Quest;
import com.greatescape.api.monolith.domain.enumeration.FearLevel;
import com.greatescape.api.monolith.domain.enumeration.QuestComplexity;
import com.greatescape.api.monolith.domain.enumeration.QuestType;
import com.greatescape.api.monolith.web.rest.QuestResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link Quest} entity. This class is used
 * in {@link QuestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestCriteria implements Serializable, Criteria {
    /**
     * Class for filtering QuestComplexity
     */
    public static class QuestComplexityFilter extends Filter<QuestComplexity> {

        public QuestComplexityFilter() {
        }

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
    public static class FearLevelFilter extends Filter<FearLevel> {

        public FearLevelFilter() {
        }

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
    public static class QuestTypeFilter extends Filter<QuestType> {

        public QuestTypeFilter() {
        }

        public QuestTypeFilter(QuestTypeFilter filter) {
            super(filter);
        }

        @Override
        public QuestTypeFilter copy() {
            return new QuestTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter slug;

    private StringFilter title;

    private IntegerFilter playersMinCount;

    private IntegerFilter playersMaxCount;

    private IntegerFilter durationInMinutes;

    private QuestComplexityFilter complexity;

    private FearLevelFilter fearLevel;

    private QuestTypeFilter type;

    private LongFilter locationId;

    private LongFilter companyId;

    private LongFilter thematicId;

    public QuestCriteria() {
    }

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

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public IntegerFilter getPlayersMinCount() {
        return playersMinCount;
    }

    public void setPlayersMinCount(IntegerFilter playersMinCount) {
        this.playersMinCount = playersMinCount;
    }

    public IntegerFilter getPlayersMaxCount() {
        return playersMaxCount;
    }

    public void setPlayersMaxCount(IntegerFilter playersMaxCount) {
        this.playersMaxCount = playersMaxCount;
    }

    public IntegerFilter getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(IntegerFilter durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public QuestComplexityFilter getComplexity() {
        return complexity;
    }

    public void setComplexity(QuestComplexityFilter complexity) {
        this.complexity = complexity;
    }

    public FearLevelFilter getFearLevel() {
        return fearLevel;
    }

    public void setFearLevel(FearLevelFilter fearLevel) {
        this.fearLevel = fearLevel;
    }

    public QuestTypeFilter getType() {
        return type;
    }

    public void setType(QuestTypeFilter type) {
        this.type = type;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getThematicId() {
        return thematicId;
    }

    public void setThematicId(LongFilter thematicId) {
        this.thematicId = thematicId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuestCriteria that = (QuestCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(title, that.title) &&
            Objects.equals(playersMinCount, that.playersMinCount) &&
            Objects.equals(playersMaxCount, that.playersMaxCount) &&
            Objects.equals(durationInMinutes, that.durationInMinutes) &&
            Objects.equals(complexity, that.complexity) &&
            Objects.equals(fearLevel, that.fearLevel) &&
            Objects.equals(type, that.type) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(thematicId, that.thematicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        slug,
        title,
        playersMinCount,
        playersMaxCount,
        durationInMinutes,
        complexity,
        fearLevel,
        type,
        locationId,
        companyId,
        thematicId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (slug != null ? "slug=" + slug + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (playersMinCount != null ? "playersMinCount=" + playersMinCount + ", " : "") +
                (playersMaxCount != null ? "playersMaxCount=" + playersMaxCount + ", " : "") +
                (durationInMinutes != null ? "durationInMinutes=" + durationInMinutes + ", " : "") +
                (complexity != null ? "complexity=" + complexity + ", " : "") +
                (fearLevel != null ? "fearLevel=" + fearLevel + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
                (thematicId != null ? "thematicId=" + thematicId + ", " : "") +
            "}";
    }

}
