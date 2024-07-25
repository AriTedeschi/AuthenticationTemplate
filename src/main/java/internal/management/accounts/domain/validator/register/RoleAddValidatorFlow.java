package internal.management.accounts.domain.validator.register;

import internal.management.accounts.application.inbound.request.RoleRegisterRequest;
import internal.management.accounts.config.exception.ValidationException;
import internal.management.accounts.domain.repository.RoleRepository;
import internal.management.accounts.domain.validator.SupportedLocales;
import internal.management.accounts.domain.validator.ValidationFlow;
import internal.management.accounts.domain.validator.Validator;
import internal.management.accounts.domain.validator.rules.RoleIdValidator;
import internal.management.accounts.domain.validator.rules.RoleNameValidator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RoleAddValidatorFlow implements ValidationFlow {
    private Map<String, String> errors = new HashMap<>();
    private Validator<Integer> initValidator;
    private String locale;

    public RoleAddValidatorFlow(RoleRegisterRequest request, String locale, RoleRepository repository){
        this.locale = locale;

        this.initValidator = new RoleIdValidator(request.roleId(), this, repository,
                             new RoleNameValidator(request.roleName(), this, null));
    }

    @Override
    public void addError(String field, String error) { this.errors.put(field,error); }

    @Override
    public void addError(Map<String, String> errors) {
        this.errors.putAll(errors);
    }

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

