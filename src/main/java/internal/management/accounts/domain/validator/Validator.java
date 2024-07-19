package internal.management.accounts.domain.validator;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class Validator<T> {
    protected ValidationFlow validation;
    private Validator<?> nextValidation;
    private String nullMessage;
    private boolean nullCheck;
    private ResourceBundle bundle;
    protected T field;

    protected Validator(T field, ValidationFlow validation, Validator<?> nextValidation, String nullMessage){
        this.field = field;
        this.validation=validation;
        this.nextValidation=nextValidation;
        this.nullMessage=nullMessage;
        this.nullCheck = true;
        this.bundle = ResourceBundle.getBundle("messages", validation.getLocale());
    }
    public void validate() {
        validateNext();
    }
    private void validateNext(){
        if(nextValidation != null)
            nextValidation.validate();
        this.nextValidation = null;
    }
    protected boolean isNull() {
        if(!nullCheck)
            return Objects.isNull(this.field);

        String fieldName = nullMessage.split(":")[0];
        String errorMessage = nullMessage.split(":")[1];
        if(Objects.isNull(this.field))
            validation.addError(fieldName, getMessage(errorMessage));
        return Objects.isNull(this.field);
    }
    protected String getMessage(String key){
        return bundle.getString(key);
    }
}
