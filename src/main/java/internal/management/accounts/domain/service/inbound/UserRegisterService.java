package internal.management.accounts.domain.service.inbound;

import internal.management.accounts.application.inbound.request.ChangePasswordRequest;
import internal.management.accounts.application.inbound.request.InternalUserRegisterRequest;
import internal.management.accounts.application.inbound.request.UserRegisterRequest;
import internal.management.accounts.application.inbound.response.NewUserPassword;
import internal.management.accounts.application.inbound.response.UserRegisterResponse;

public interface UserRegisterService {
    UserRegisterResponse register(UserRegisterRequest request, String locale);
    UserRegisterResponse addInternalUser(InternalUserRegisterRequest request, String locale);

    NewUserPassword changePassword(ChangePasswordRequest passwordRequest, String lang);

    NewUserPassword changePassword();
}
