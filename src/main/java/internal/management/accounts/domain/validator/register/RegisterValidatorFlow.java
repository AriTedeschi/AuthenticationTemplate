package internal.management.accounts.domain.validator.register;

import internal.management.accounts.application.inbound.request.UserRegisterRequest;
import internal.management.accounts.config.exception.ValidationException;
import internal.management.accounts.domain.model.vo.NameVO;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.validator.SupportedLocales;
import internal.management.accounts.domain.validator.ValidationFlow;
import internal.management.accounts.domain.validator.Validator;
import internal.management.accounts.domain.validator.rules.EmailRegisterValidator;
import internal.management.accounts.domain.validator.rules.NameRegisterValidator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterValidatorFlow implements ValidationFlow {
    private Map<String, String> errors = new HashMap<>();
    private Validator<String> initValidator;
    private String locale;

    public RegisterValidatorFlow(UserRegisterRequest request, String locale, UserRepository repository){
        NameVO name = new NameVO(request.firstName(),request.lastName());
        this.locale = locale;

        this.initValidator = new EmailRegisterValidator(request.email(),this, repository,
                             new NameRegisterValidator(name, this, null));
    }

    @Override
    public void addError(String field, String error) { this.errors.put(field,error); }

    @Override
    public Locale getLocale() {
        return SupportedLocales.fromKey(this.locale);
    }

    public boolean containsError(){return !this.errors.isEmpty();}

    public void validate() {
        this.initValidator.validate();
        if(containsError())
            throw new ValidationException(errors);
    }
}

