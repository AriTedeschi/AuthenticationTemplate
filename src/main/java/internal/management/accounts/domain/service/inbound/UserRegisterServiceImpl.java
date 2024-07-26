package internal.management.accounts.domain.service.inbound;

import internal.management.accounts.application.inbound.adapter.InternalUserRegisterRequest2UserEntity;
import internal.management.accounts.application.inbound.adapter.InternalUserRegisterRequest2UserRegisterRequest;
import internal.management.accounts.application.inbound.adapter.UserEntity2UserRegisterResponse;
import internal.management.accounts.application.inbound.adapter.UserRegisterRequest2UserEntity;
import internal.management.accounts.application.inbound.request.ChangePasswordRequest;
import internal.management.accounts.application.inbound.request.InternalUserRegisterRequest;
import internal.management.accounts.application.inbound.request.UserRegisterRequest;
import internal.management.accounts.application.inbound.response.NewUserPassword;
import internal.management.accounts.application.inbound.response.UserRegisterResponse;
import internal.management.accounts.config.exception.QueryException;
import internal.management.accounts.config.exception.UserCodeOverflowException;
import internal.management.accounts.config.security.AuthenticationToken;
import internal.management.accounts.config.utility.RandomPasswordGenerator;
import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.model.vo.PasswordVO;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.service.outbound.factory.UserLookupFactory;
import internal.management.accounts.domain.validator.SupportedLocales;
import internal.management.accounts.domain.validator.register.UserRegisterValidatorFlow;
import internal.management.accounts.domain.validator.rules.RoleIdValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
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
            new UserRegisterValidatorFlow(request, 0, locale, repository).validate();
            UserEntity entity = new UserRegisterRequest2UserEntity(request).getInstance();
            entity.getUserCode().set(repository.generateUserCode());
            return new UserEntity2UserRegisterResponse(repository.save(entity), false).getInstance();
        } catch (DataAccessException e) {
            if (e.getMessage().contains("Achieved max user_code limit"))
                throw new UserCodeOverflowException(SupportedLocales.getMessage(locale, USER_CODE_OVERFLOW));
            log.error(e.getMessage());
            throw new QueryException("Error while accessing data layer");
        }
    }

    @Override
    public UserRegisterResponse addInternalUser(InternalUserRegisterRequest request, String locale) {
        UserRegisterRequest validationDTO = new  InternalUserRegisterRequest2UserRegisterRequest(request).getInstance();
        new UserRegisterValidatorFlow(validationDTO, request.roleId(), locale, repository).validate();
        new RoleIdValidator(request.roleId()).stepValidate();

        String userId = ((AuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        String loggedPrefix= UserLookupFactory.getBy(userId, request.roleId(), repository).getUserCode().getAccountPrefix();
        String roleCode = String.format("%05d", request.roleId());
        log.info("Creating subUser with Prefix:{} roleCode:{}",loggedPrefix,roleCode);

        UserEntity newEntity = new InternalUserRegisterRequest2UserEntity(request).getInstance();
        Integer nextSubUserId = repository.getNextSubUserId(request.roleId(), loggedPrefix)
                .orElse(1);
        log.info("Checking generated subUserCode {}", nextSubUserId);
        new RoleIdValidator(nextSubUserId).stepValidate();

        newEntity.getUserCode().setFromUsercode(loggedPrefix, roleCode, String.format("%05d", nextSubUserId));

        return new UserEntity2UserRegisterResponse(repository.save(newEntity), false).getInstance();
    }

    @Override
    public NewUserPassword changePassword(ChangePasswordRequest passwordRequest, String lang) {
        UserEntity userEntity = UserLookupFactory.getBy(passwordRequest.login(), passwordRequest.roleId(), repository);
        String newPassword = RandomPasswordGenerator.generateRandomPassword(12);
        userEntity.setTokenVersion(userEntity.getTokenVersion()+1);
        userEntity.getPassword().encode(newPassword);
        repository.save(userEntity);
        return new NewUserPassword(userEntity.getUuid(),userEntity.getUserCode().get(),newPassword);
    }

    @Override
    public NewUserPassword changePassword() {
        String userId = ((AuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        return changePassword(new ChangePasswordRequest(userId, null), null);
    }
}
