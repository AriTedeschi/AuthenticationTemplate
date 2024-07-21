package internal.management.accounts.application.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
        summary = "Default summary",
        responses = {
                @ApiResponse(responseCode = "201", description = "Successful", content = @Content(schema = @Schema(implementation = Object.class))),
                @ApiResponse(responseCode = "422", description = "Field validation error format", content = @Content(schema = @Schema(implementation = Object.class))),
                @ApiResponse(responseCode = "500", description = "Internal application error format")
        }
)
public @interface RegisterOperation {
    String summary() default "Default summary";
    Class<?> responseClass() default Object.class;
    Class<?> errorClass() default Object.class;
}
