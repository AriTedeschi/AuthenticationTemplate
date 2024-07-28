package internal.management.accounts.application.inbound.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User registration request")
public record UserRegisterRequest(
        @Schema(description = "User`s communication email", example = "jonh.doe@gmail.com")
        String email,
        @Schema(description = "User`s password", example = "123")
        String password,
        @Schema(description = "User`s first name", example = "Jonh")
        String firstName,
        @Schema(description = "User`s last name", example = "Doe")
        String lastName) {}
