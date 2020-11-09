package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.enumeration.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link Player} entity.
 */
@ApiModel(description = "@TODO: should be audited")
@Getter
@Setter
public final class PlayerDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "^\\d+$")
    private String phone;

    @NotNull
    @Pattern(regexp = "^\\S+@\\S+$")
    private String email;

    private LocalDate birthday;

    private Gender gender;

    private boolean subscriptionAllowed;

    /**
     * Mapping Application user (Player) to default jHipster's one
     */
    @ApiModelProperty(value = "Mapping Application user (Player) to default jHipster's one")
    private Long internalUserId;

    private String internalUserLogin;

    private Long companyId;

    private String companyTitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerDTO)) {
            return false;
        }

        return id != null && id.equals(((PlayerDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", gender='" + getGender() + "'" +
            ", subscriptionAllowed='" + isSubscriptionAllowed() + "'" +
            ", internalUserId=" + getInternalUserId() +
            ", internalUserLogin='" + getInternalUserLogin() + "'" +
            ", companyId=" + getCompanyId() +
            ", companyTitle='" + getCompanyTitle() + "'" +
            "}";
    }
}
