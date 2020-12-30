package com.greatescape.api.monolith.service.dto;

import com.greatescape.api.monolith.domain.Player;
import com.greatescape.api.monolith.domain.enumeration.Gender;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * A DTO for the {@link Player} entity.
 */
@Data
public final class PlayerDTO {

    private UUID id;

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
    private UUID internalUserId;

    private String internalUserLogin;

    private UUID companyId;

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
        // For more info see: https://bit.ly/37Zo2W3
        return getClass().hashCode();
    }
}
