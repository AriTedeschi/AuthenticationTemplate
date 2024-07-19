package internal.management.accounts.domain.validator.rules;

import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.validator.ValidationFlow;
import internal.management.accounts.domain.validator.Validator;

public class EmailSearchValidator extends Validator<String> {
    private String email;
    private UserRepository repository;

    public EmailSearchValidator(String email, UserRepository repository, ValidationFlow registerValidator, Validator<?> nextValidation){
        super(email,registerValidator, nextValidation,"email:Provide an email!");
        this.repository = repository;
        this.email=email;
    }

    @Override
    public void validate(){
        super.validate();
        if(super.isNull())
            return;
        if(repository.findByEmail(email).isPresent())
            return;
        validation.addError("email","Provided email not registered!");
    }
}