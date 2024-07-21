package internal.management.accounts.domain.validator.rules;

import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.validator.ValidationFlow;
import internal.management.accounts.domain.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailRegisterValidator extends Validator<String> {
    @Value("${regex.email}")
    private String emailRegex;
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

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches())
            validation.addError("email",getMessage("register.email.syntax"));
        if(repository.findByEmail(email).isPresent())
            validation.addError("email",getMessage("register.email.used"));
    }

    public void setEmailRegex(String emailRegex) {
        this.emailRegex = emailRegex;
    }
}