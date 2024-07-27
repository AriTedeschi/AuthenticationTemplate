package internal.management.accounts.config.exception;

import java.util.Map;

public class TokenExpiredException extends RuntimeException {
    private static final String FIELD="bearToken";
    private final String message;

    public TokenExpiredException(String message) {
        super(message);
        this.message = message;
    }

    public Map<String, String> convert() { return Map.of(FIELD,message); }
}
