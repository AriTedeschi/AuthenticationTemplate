package internal.management.accounts.application.inbound.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Role registration")
public record RoleRegisterRequest(
        @Schema(description = "Role`s identifier, userCode infix", maxLength = 99999, example = "1")
        Integer roleId,
        @Schema(description = "Role`s name, description of role", example = "Registration admin")
        String roleName) {}
