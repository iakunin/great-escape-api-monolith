package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Thematic;
import com.greatescape.api.monolith.web.rest.ThematicResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link Thematic} entity. This class is used
 * in {@link ThematicResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /thematics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ThematicCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter slug;

    private StringFilter title;

    private LongFilter questId;

    public ThematicCriteria() {
    }

    public ThematicCriteria(ThematicCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.slug = other.slug == null ? null : other.slug.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.questId = other.questId == null ? null : other.questId.copy();
    }

    @Override
    public ThematicCriteria copy() {
        return new ThematicCriteria(this);
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
        final ThematicCriteria that = (ThematicCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(title, that.title) &&
            Objects.equals(questId, that.questId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        slug,
        title,
        questId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThematicCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (slug != null ? "slug=" + slug + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (questId != null ? "questId=" + questId + ", " : "") +
            "}";
    }

}
