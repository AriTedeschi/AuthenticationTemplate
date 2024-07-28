package internal.management.accounts.application.inbound.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User Register Response")
public record UserRegisterResponse(
        @Schema(description = "Unique identifier generated for new User", maxLength = 36, example = "0491703b-1eb9-4616-8c50-2d62e0ca886d")
        String userId,
        @Schema(description = "Unique Code mask generated for new User", maxLength = 17, example = "77777-00000-00000")
        String userCode,
        @Schema(description = "User provided email", example = "jonh.doe@gmail.com")
        String email,
        @Schema(description = "User fullname", example = "Jonh Doe")
        String fullname) {}
