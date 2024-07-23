package internal.management.accounts.domain.validator.rules;

import internal.management.accounts.domain.repository.RoleRepository;
import internal.management.accounts.domain.validator.ValidationFlow;
import internal.management.accounts.domain.validator.Validator;

import java.util.Objects;

public class RoleIdValidator extends Validator<Integer> {
    private RoleRepository repository;

    public RoleIdValidator(Integer roleId, ValidationFlow registerValidator, RoleRepository repository, Validator<String> nextValidation){
        super(roleId, registerValidator, nextValidation,"name:register.name.null");
        this.repository = repository;
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
        if(field < 0)
            validation.addError("roleId",getMessage("register.roleId.negative"));
        if(field > 99999)
            validation.addError("roleId",getMessage("register.roleId.overflow"));

    }
}