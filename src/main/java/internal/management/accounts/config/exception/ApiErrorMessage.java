package internal.management.accounts.config.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ApiErrorMessage {

    private final Map<String,String> errors;

    public ApiErrorMessage(Map<String,String> errors) {
        this.errors = errors;
    }
    public ApiErrorMessage(String error) {
        errors = Map.of("message", error);
    }
}
