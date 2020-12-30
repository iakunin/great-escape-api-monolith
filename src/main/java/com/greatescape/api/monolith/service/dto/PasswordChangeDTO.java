package com.greatescape.api.monolith.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representing a password change required data - current and new password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class PasswordChangeDTO {

    private String currentPassword;

    private String newPassword;
}
