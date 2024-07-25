package internal.management.accounts.domain.validator.rules;

import internal.management.accounts.config.exception.ValidationException;
import internal.management.accounts.domain.repository.RoleRepository;
import internal.management.accounts.domain.validator.ValidationFlow;
import internal.management.accounts.domain.validator.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RoleIdValidator extends Validator<Integer> {
    private RoleRepository repository;

    public RoleIdValidator(Integer roleId, ValidationFlow registerValidator, RoleRepository repository, Validator<String> nextValidation){
        super(roleId, registerValidator, nextValidation,"roleId:register.roleId.null");
        this.repository = repository;
    }

    public RoleIdValidator(Integer roleId){
        super(roleId);
    }

    @Override
    public void validate(){
        super.validate();
        if(super.isNull())
            return;

        if(Objects.isNull(field)){
            validation.addError("roleId", String.format(getMessage("register.name.one.null"), "roleId"));
            return;
        }

        repository.findById(field).ifPresent(id -> validation.addError("roleId",getMessage("register.roleId.used")));
        validation.addError(validateLimits());
    }

    public Map<String, String> validateLimits(){
        Map<String, String> errors = new HashMap<>();
        if(field < 0)
            errors.put("roleId",getMessage("register.roleId.negative"));
        if(field > 99999)
            errors.put("roleId",getMessage("register.roleId.overflow"));
        return errors;
    }

    public void stepValidate(){
        Map<String, String> errors = validateLimits();
        if(!errors.isEmpty())
            throw new ValidationException(errors);
    }
}