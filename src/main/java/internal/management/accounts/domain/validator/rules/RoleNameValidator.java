package internal.management.accounts.domain.validator.rules;

import internal.management.accounts.application.inbound.request.RoleRegisterRequest;
import internal.management.accounts.domain.repository.RoleRepository;
import internal.management.accounts.domain.validator.ValidationFlow;
import internal.management.accounts.domain.validator.Validator;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoleNameValidator extends Validator<String> {
    private static final String NAME_REGEX = "^[a-zA-Z0-9\\s]*$";

    public RoleNameValidator(String request, ValidationFlow registerValidator, Validator<Integer> nextValidation){
        super(request, registerValidator, nextValidation,"name:register.name.null");
    }

    @Override
    public void validate(){
        super.validate();
        if(super.isNull())
            return;

        if(Objects.isNull(field)){
            validation.addError("roleName", String.format(getMessage("register.name.one.null"), "roleName"));
            return;
        }

        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(field);
        if(!matcher.matches())
            validation.addError("roleName",String.format(getMessage("register.name.syntax"), "roleName"));
    }
}