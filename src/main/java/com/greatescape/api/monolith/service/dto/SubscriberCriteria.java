package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Subscriber;
import com.greatescape.api.monolith.web.rest.SubscriberResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link Subscriber} entity. This class is used
 * in {@link SubscriberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /subscribers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SubscriberCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter email;

    public SubscriberCriteria() {
    }

    public SubscriberCriteria(SubscriberCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.email = other.email == null ? null : other.email.copy();
    }

    @Override
    public SubscriberCriteria copy() {
        return new SubscriberCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubscriberCriteria that = (SubscriberCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        email
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriberCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
            "}";
    }

}
