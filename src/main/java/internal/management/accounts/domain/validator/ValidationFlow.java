package internal.management.accounts.domain.validator;

public interface ValidationFlow {
    void addError(String field, String error);
}
