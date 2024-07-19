package internal.management.accounts.application.inbound;

import internal.management.accounts.config.exception.UserCodeOverflowException;
import internal.management.accounts.config.exception.ValidationException;
import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.application.inbound.adapter.UserEntity2UserRegisterResponse;
import internal.management.accounts.application.inbound.adapter.UserRegisterRequest2UserEntity;
import internal.management.accounts.application.inbound.request.UserRegisterRequest;
import internal.management.accounts.application.inbound.response.UserRegisterResponse;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.service.inbound.UserRegisterService;
import internal.management.accounts.domain.validator.SupportedLocales;
import internal.management.accounts.domain.validator.register.RegisterValidatorFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
@Service
public class UserRegisterServiceImpl implements UserRegisterService {
    private static final String DEFAULT_ERROR = "Something went wrong!";
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
                throw new UserCodeOverflowException(getErrorMessage(locale, USER_CODE_OVERFLOW));
            //TODO Criar nova exception para tratar erro de banco
            log.error(e.getMessage());
            throw new ValidationException(Map.of("database","Error while accessing data layer"));
        }
    }

    private String getErrorMessage(String locale, String key){
        if (key == null || locale == null)
            return DEFAULT_ERROR;

        try {
            Locale loc = SupportedLocales.fromKey(locale);
            ResourceBundle bundle = ResourceBundle.getBundle("messages", loc);

            return Optional.ofNullable(bundle.getString(key))
                    .orElse(DEFAULT_ERROR);
        } catch (Exception e) {
            return DEFAULT_ERROR;
        }
    }
}
