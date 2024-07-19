package internal.management.accounts.domain.validator;

import java.util.Arrays;
import java.util.Locale;

public enum SupportedLocales {
    PORTUGUESE("pt", new Locale.Builder().setLanguage("pt").build()),
    ENGLISH("en", new Locale.Builder().setLanguage("en").build());

    private final String key;
    private final Locale locale;

    SupportedLocales(String key, Locale locale) {
        this.key = key;
        this.locale = locale;
    }

    public String getKey() {
        return key;
    }

    public Locale getLocale() {
        return locale;
    }

    public static Locale fromKey(String key) {
        return Arrays.stream(values())
                .filter(locale -> locale.getKey().equalsIgnoreCase(key))
                .map(SupportedLocales::getLocale)
                .findFirst()
                .orElse(ENGLISH.getLocale());
    }
}
