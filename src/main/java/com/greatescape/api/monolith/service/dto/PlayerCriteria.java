package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.enumeration.Gender;
import com.greatescape.api.monolith.web.rest.admin.PlayerResource;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Criteria class for the {@link Player} entity. This class is used
 * in {@link PlayerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /players?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public final class PlayerCriteria implements Criteria {

    /**
     * Class for filtering Gender
     */
    @NoArgsConstructor
    public static class GenderFilter extends Filter<Gender> {

        private static final long serialVersionUID = 1L;

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }

    }

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter phone;

    private StringFilter email;

    private LocalDateFilter birthday;

    private GenderFilter gender;

    private BooleanFilter subscriptionAllowed;

    private UUIDFilter internalUserId;

    private UUIDFilter companyId;

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
}
