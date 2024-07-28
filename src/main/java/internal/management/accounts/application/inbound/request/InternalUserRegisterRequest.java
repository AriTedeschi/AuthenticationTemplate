package internal.management.accounts.application.inbound.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Internal user registration request")
public record InternalUserRegisterRequest(
        @Schema(description = "User`s communication email", example = "jonh.doe@gmail.com")
        String email,
        @Schema(description = "User`s password", example = "123")
        String password,
        @Schema(description = "User`s first name", example = "Jonh")
        String firstName,
        @Schema(description = "User`s last name", example = "Doe")
        String lastName,
        @Schema(description = "User`s role which defines userCode infix", example = "1")
        Integer roleId) {}
