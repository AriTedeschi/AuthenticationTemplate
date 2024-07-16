package internal.management.accounts.domain.service;

import internal.management.accounts.domain.model.request.UserRegisterRequest;
import internal.management.accounts.domain.model.response.UserRegisterResponse;

public interface UserService {
    UserRegisterResponse register(UserRegisterRequest request);
}
