package internal.management.accounts.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class ApiErrorMessage {

    private HttpStatus status;
    private Map<String,String> errors;

    public ApiErrorMessage(HttpStatus status, Map<String,String> errors) {
        this.status = status;
        this.errors = errors;
    }
    public ApiErrorMessage(HttpStatus status, String error) {
        this.status = status;
        errors = Map.of("message", error);
    }
}
