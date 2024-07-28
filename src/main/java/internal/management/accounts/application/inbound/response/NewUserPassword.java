package internal.management.accounts.application.inbound.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "New Password Response")
public record NewUserPassword(
        @Schema(description = "Unique identifier of the User", example = "0491703b-1eb9-4616-8c50-2d62e0ca886d")
        String userId,
        @Schema(description = "Unique Code mask of the user", example = "77777-00000-00000")
        String userCode,
        @Schema(description = "New password, showed once", maxLength = 12, example = "@$`asdf")
        String password) {}
