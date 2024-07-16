package internal.management.accounts.domain.validator.register;

import internal.management.accounts.domain.model.request.UserRegisterRequest;
import internal.management.accounts.domain.model.vo.NameVO;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.validator.ValidationFlow;
import internal.management.accounts.domain.validator.Validator;
import internal.management.accounts.domain.validator.rules.EmailRegisterValidator;
import internal.management.accounts.domain.validator.rules.NameRegisterValidator;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

public class RequestValidatorFlow implements ValidationFlow {
    private Map<String, String> errors = new HashMap<>();
    private Validator<String> initValidator;

    public RequestValidatorFlow(UserRegisterRequest request, UserRepository repository){
        NameVO name = new NameVO(request.firstName(),request.lastName());

        this.initValidator = new EmailRegisterValidator(request.email(),this, repository,
                             new NameRegisterValidator(name, this, null));
    }

    public void addError(String field, String error){this.errors.put(field,error);}
    public boolean containsError(){return !this.errors.isEmpty();}

    public void validate() {
        this.initValidator.validate();
        if(containsError()){
            StringBuilder errorMessage = new StringBuilder("Errors occurred in the following fields:\n");
            errors.forEach((field, msg) ->
                    errorMessage.append(field).append(": ").append(msg).append("\n")
            );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());
        }
    }
}

