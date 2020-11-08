package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.enumeration.Gender;
import com.greatescape.api.monolith.web.rest.PlayerResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link Player} entity. This class is used
 * in {@link PlayerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /players?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlayerCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {
        }

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter phone;

    private StringFilter email;

    private LocalDateFilter birthday;

    private GenderFilter gender;

    private BooleanFilter subscriptionAllowed;

    private LongFilter internalUserId;

    private LongFilter companyId;

    public PlayerCriteria() {
    }

    public PlayerCriteria(PlayerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.birthday = other.birthday == null ? null : other.birthday.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.subscriptionAllowed = other.subscriptionAllowed == null ? null : other.subscriptionAllowed.copy();
        this.internalUserId = other.internalUserId == null ? null : other.internalUserId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
    }

    @Override
    public PlayerCriteria copy() {
        return new PlayerCriteria(this);
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

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LocalDateFilter getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateFilter birthday) {
        this.birthday = birthday;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public BooleanFilter getSubscriptionAllowed() {
        return subscriptionAllowed;
    }

    public void setSubscriptionAllowed(BooleanFilter subscriptionAllowed) {
        this.subscriptionAllowed = subscriptionAllowed;
    }

    public LongFilter getInternalUserId() {
        return internalUserId;
    }

    public void setInternalUserId(LongFilter internalUserId) {
        this.internalUserId = internalUserId;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlayerCriteria that = (PlayerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(birthday, that.birthday) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(subscriptionAllowed, that.subscriptionAllowed) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(companyId, that.companyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        phone,
        email,
        birthday,
        gender,
        subscriptionAllowed,
        internalUserId,
        companyId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (birthday != null ? "birthday=" + birthday + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (subscriptionAllowed != null ? "subscriptionAllowed=" + subscriptionAllowed + ", " : "") +
                (internalUserId != null ? "internalUserId=" + internalUserId + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
            "}";
    }

}
