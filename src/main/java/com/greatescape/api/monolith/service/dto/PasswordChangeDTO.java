package com.greatescape.api.monolith.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO representing a password change required data - current and new password.
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeDTO {

    private String currentPassword;

    private String newPassword;

    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
