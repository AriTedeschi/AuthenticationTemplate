package internal.management.accounts.domain.validator;

import java.util.Locale;

public interface ValidationFlow {
    void addError(String field, String error);
    Locale getLocale();
}
