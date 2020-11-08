package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.enumeration.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * A DTO for the {@link Player} entity.
 */
@ApiModel(description = "@TODO: should be audited")
public class PlayerDTO implements Serializable {

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

    private Boolean subscriptionAllowed;

    /**
     * Mapping Application user (Player) to default jHipster's one
     */
    @ApiModelProperty(value = "Mapping Application user (Player) to default jHipster's one")

    private Long internalUserId;

    private String internalUserLogin;

    private Long companyId;

    private String companyTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Boolean isSubscriptionAllowed() {
        return subscriptionAllowed;
    }

    public void setSubscriptionAllowed(Boolean subscriptionAllowed) {
        this.subscriptionAllowed = subscriptionAllowed;
    }

    public Long getInternalUserId() {
        return internalUserId;
    }

    public void setInternalUserId(Long userId) {
        this.internalUserId = userId;
    }

    public String getInternalUserLogin() {
        return internalUserLogin;
    }

    public void setInternalUserLogin(String userLogin) {
        this.internalUserLogin = userLogin;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyTitle() {
        return companyTitle;
    }

    public void setCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle;
    }

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
