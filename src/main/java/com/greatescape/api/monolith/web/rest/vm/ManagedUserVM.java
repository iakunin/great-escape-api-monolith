package com.greatescape.api.monolith.web.rest.vm;

import com.greatescape.api.monolith.service.dto.UserDTO;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
@Getter
@Setter
@NoArgsConstructor
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
