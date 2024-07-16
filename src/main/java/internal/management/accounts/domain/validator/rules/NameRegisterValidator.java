package internal.management.accounts.domain.validator.rules;

import internal.management.accounts.domain.model.vo.NameVO;
import internal.management.accounts.domain.validator.ValidationFlow;
import internal.management.accounts.domain.validator.Validator;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameRegisterValidator extends Validator<NameVO> {
    private static final String NAME_REGEX = "^[a-zA-Z0-9\\s]*$";
    private NameVO name;

    public NameRegisterValidator(NameVO name, ValidationFlow registerValidator, Validator<Integer> nextValidation){
        super(name, registerValidator, nextValidation,"name:Provide a name!");
        this.name=name;
    }

    @Override
    public void validate(){
        super.validate();
        if(super.isNull())
            return;

        validateSintax(name.getFirstName(),"firstName");
        validateSintax(name.getLastName(),"lastName");
    }

    private void validateSintax(String field, String fieldName) {
        if(Objects.isNull(field)){
            registerValidator.addError(fieldName,"Provide a "+fieldName);
            return;
        }

        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(field);
        if(!matcher.matches())
            registerValidator.addError("name",fieldName+" syntax error, please provide a correct name");
    }
}