package internal.management.accounts.domain.validator;
import java.util.Objects;

public abstract class Validator<T> {
    protected ValidationFlow registerValidator;
    private Validator<?> nextValidation;
    private String nullMessage;
    private boolean nullCheck;
    protected T field;

    protected Validator(T field, ValidationFlow registerValidator, Validator<?> nextValidation, String nullMessage){
        this.field = field;
        this.registerValidator=registerValidator;
        this.nextValidation=nextValidation;
        this.nullMessage=nullMessage;
        this.nullCheck = true;
    }


//    Validator without nullcheck
//    protected Validator(T field, ValidationFlow registerValidator, Validator<?> nextValidation){
//        this.field = field;
//        this.registerValidator=registerValidator;
//        this.nextValidation=nextValidation;
//        this.nullCheck = false;
//    }

    public void validate() {
        validateNext();
    }
    private void validateNext(){
        if(nextValidation != null)
            nextValidation.validate();
    }
    protected boolean isNull() {
        if(!nullCheck)
            return Objects.isNull(this.field);

        String fieldName = nullMessage.split(":")[0];
        String errorMessage = nullMessage.split(":")[1];
        if(Objects.isNull(this.field))
            registerValidator.addError(fieldName,errorMessage);
        return Objects.isNull(this.field);
    }
}
