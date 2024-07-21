package internal.management.accounts.domain.validator.rules;

import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.validator.ValidationFlow;
import internal.management.accounts.domain.validator.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailRegisterValidator extends Validator<String> {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private final String email;
    private final UserRepository repository;

    public EmailRegisterValidator(String email, ValidationFlow validation, UserRepository repository, Validator<?> nextValidation){
        super(email,validation, nextValidation,"email:register.email.null");
        this.repository = repository;
        this.email=email;
    }

    @Override
    public void validate(){
        super.validate();
        if(super.isNull())
            return;

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches())
            validation.addError("email",getMessage("register.email.syntax"));
        if(repository.findByEmail(email).isPresent())
            validation.addError("email",getMessage("register.email.used"));
    }
}