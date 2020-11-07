package com.greatescape.backend.service.dto;

import com.greatescape.backend.domain.enumeration.QuestIntegrationType;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.greatescape.backend.domain.QuestIntegrationSetting} entity. This class is used
 * in {@link com.greatescape.backend.web.rest.QuestIntegrationSettingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quest-integration-settings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestIntegrationSettingCriteria implements Serializable, Criteria {
    /**
     * Class for filtering QuestIntegrationType
     */
    public static class QuestIntegrationTypeFilter extends Filter<QuestIntegrationType> {

        public QuestIntegrationTypeFilter() {
        }

        public QuestIntegrationTypeFilter(QuestIntegrationTypeFilter filter) {
            super(filter);
        }

        @Override
        public QuestIntegrationTypeFilter copy() {
            return new QuestIntegrationTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private QuestIntegrationTypeFilter type;

    private LongFilter questId;

    public QuestIntegrationSettingCriteria() {
    }

    public QuestIntegrationSettingCriteria(QuestIntegrationSettingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.questId = other.questId == null ? null : other.questId.copy();
    }

    @Override
    public QuestIntegrationSettingCriteria copy() {
        return new QuestIntegrationSettingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public QuestIntegrationTypeFilter getType() {
        return type;
    }

    public void setType(QuestIntegrationTypeFilter type) {
        this.type = type;
    }

    public LongFilter getQuestId() {
        return questId;
    }

    public void setQuestId(LongFilter questId) {
        this.questId = questId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuestIntegrationSettingCriteria that = (QuestIntegrationSettingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(questId, that.questId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        questId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestIntegrationSettingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (questId != null ? "questId=" + questId + ", " : "") +
            "}";
    }

}
