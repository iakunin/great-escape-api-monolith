package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.QuestIntegrationSetting;
import com.greatescape.api.monolith.domain.enumeration.QuestIntegrationType;
import com.greatescape.api.monolith.web.rest.admin.QuestIntegrationSettingResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.UUIDFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link QuestIntegrationSetting} entity. This class is used
 * in {@link QuestIntegrationSettingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quest-integration-settings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public final class QuestIntegrationSettingCriteria implements Serializable, Criteria {
    /**
     * Class for filtering QuestIntegrationType
     */
    @NoArgsConstructor
    public static class QuestIntegrationTypeFilter extends Filter<QuestIntegrationType> {

        public QuestIntegrationTypeFilter(QuestIntegrationTypeFilter filter) {
            super(filter);
        }

        @Override
        public QuestIntegrationTypeFilter copy() {
            return new QuestIntegrationTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private QuestIntegrationTypeFilter type;

    private UUIDFilter questId;

    public QuestIntegrationSettingCriteria(QuestIntegrationSettingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.questId = other.questId == null ? null : other.questId.copy();
    }

    @Override
    public QuestIntegrationSettingCriteria copy() {
        return new QuestIntegrationSettingCriteria(this);
    }
}
