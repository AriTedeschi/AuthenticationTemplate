package internal.management.accounts.domain.validator;

import java.util.Locale;
import java.util.Map;

public interface ValidationFlow {
    void addError(String field, String error);
    void addError(Map<String,String> errors);
    Locale getLocale();
}
