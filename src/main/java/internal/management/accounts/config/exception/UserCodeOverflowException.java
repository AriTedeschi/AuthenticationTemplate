package internal.management.accounts.config.exception;

import java.util.Map;

public class UserCodeOverflowException extends RuntimeException {
    private static final String FIELD="userCode";
    private final String message;

    public UserCodeOverflowException(String message) {
        this.message = message;
    }

    public Map<String, String> convert() { return Map.of(FIELD,message); }
}
