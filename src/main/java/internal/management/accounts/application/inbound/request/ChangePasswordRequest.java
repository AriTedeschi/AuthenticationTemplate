package internal.management.accounts.application.inbound.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User`s password change request")
public record ChangePasswordRequest(
        @Schema(description = "User`s login identifier, can be either userCode or userId",
                maxLength = 36,
                example = "77777-00000-00000")
        String login) {}
