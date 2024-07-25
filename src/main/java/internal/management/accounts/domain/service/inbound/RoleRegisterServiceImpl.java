package internal.management.accounts.domain.service.inbound;

import internal.management.accounts.application.inbound.request.RoleRegisterRequest;
import internal.management.accounts.domain.model.RoleEntity;
import internal.management.accounts.domain.repository.RoleRepository;
import internal.management.accounts.domain.validator.register.RoleAddValidatorFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleRegisterServiceImpl implements RoleRegisterService {
    private RoleRepository repository;

    public RoleRegisterServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public RoleRegisterRequest addMember(RoleRegisterRequest request, String locale){
        new RoleAddValidatorFlow(request, locale, repository).validate();
        RoleEntity roleEntity = RoleEntity.builder().id(request.roleId())
                                .name(request.roleName()).build();
        roleEntity = repository.save(roleEntity);
        return new RoleRegisterRequest(roleEntity.getId(),roleEntity.getName());
    }
}