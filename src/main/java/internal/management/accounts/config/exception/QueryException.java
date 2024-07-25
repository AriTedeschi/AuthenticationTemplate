package internal.management.accounts.config.exception;

import java.util.Map;

public class QueryException extends RuntimeException {
    private static final String FIELD="DataLayer";
    private final String message;

    public QueryException(String message) {
        this.message = message;
    }

    public Map<String, String> convert() { return Map.of(FIELD,message); }
}
