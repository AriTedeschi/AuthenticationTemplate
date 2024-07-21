package internal.management.accounts.domain.service.inbound;

import internal.management.accounts.application.inbound.adapter.UserEntity2UserRegisterResponse;
import internal.management.accounts.application.inbound.adapter.UserRegisterRequest2UserEntity;
import internal.management.accounts.application.inbound.request.UserRegisterRequest;
import internal.management.accounts.application.inbound.response.UserRegisterResponse;
import internal.management.accounts.config.exception.QueryException;
import internal.management.accounts.config.exception.UserCodeOverflowException;
import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.validator.SupportedLocales;
import internal.management.accounts.domain.validator.register.RegisterValidatorFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    private static final String USER_CODE_OVERFLOW = "register.userCode.overflow";
    private UserRepository repository;

    public UserRegisterServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserRegisterResponse register(UserRegisterRequest request, String locale) {
        try {
            new RegisterValidatorFlow(request, locale, repository).validate();
            UserEntity entity = new UserRegisterRequest2UserEntity(request).getInstance();
            entity.getUserCode().set(repository.generateUserCode());
            return new UserEntity2UserRegisterResponse(repository.save(entity), false).getInstance();
        } catch (DataAccessException e) {
            if (e.getMessage().contains("Achieved max user_code limit of"))
                throw new UserCodeOverflowException(SupportedLocales.getMessage(locale, USER_CODE_OVERFLOW));
            log.error(e.getMessage());
            throw new QueryException("Error while accessing data layer");
        }
    }
}
