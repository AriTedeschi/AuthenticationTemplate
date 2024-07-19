package internal.management.accounts.domain.service.inbound;

import internal.management.accounts.application.inbound.request.UserRegisterRequest;
import internal.management.accounts.application.inbound.response.UserRegisterResponse;

public interface UserRegisterService {
    UserRegisterResponse register(UserRegisterRequest request, String locale);
}
