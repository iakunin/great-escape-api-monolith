package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.QuestPhoto;
import com.greatescape.api.monolith.web.rest.QuestPhotoResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link QuestPhoto} entity. This class is used
 * in {@link QuestPhotoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quest-photos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public final class QuestPhotoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter url;

    private UUIDFilter questId;

    public QuestPhotoCriteria(QuestPhotoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.questId = other.questId == null ? null : other.questId.copy();
    }

    @Override
    public QuestPhotoCriteria copy() {
        return new QuestPhotoCriteria(this);
    }
}
