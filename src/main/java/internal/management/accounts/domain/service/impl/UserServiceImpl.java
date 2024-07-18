package internal.management.accounts.domain.service.impl;

import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.model.adapter.UserEntity2UserRegisterResponse;
import internal.management.accounts.domain.model.adapter.UserRegisterRequest2UserEntity;
import internal.management.accounts.domain.model.request.UserRegisterRequest;
import internal.management.accounts.domain.model.response.UserRegisterResponse;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.service.UserService;
import internal.management.accounts.domain.validator.register.RegisterValidatorFlow;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        new RegisterValidatorFlow(request, repository).validate();

        UserEntity entity = new UserRegisterRequest2UserEntity(request).getInstance();
        entity.getUserCode().set(repository.generateUserCode());
        return new UserEntity2UserRegisterResponse(repository.save(entity), false).getInstance();
    }
}
