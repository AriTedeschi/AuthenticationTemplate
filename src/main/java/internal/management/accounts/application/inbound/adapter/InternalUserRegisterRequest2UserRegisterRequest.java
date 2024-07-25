package internal.management.accounts.application.inbound.adapter;

import internal.management.accounts.application.inbound.request.InternalUserRegisterRequest;
import internal.management.accounts.application.inbound.request.UserRegisterRequest;

public class InternalUserRegisterRequest2UserRegisterRequest {
    private UserRegisterRequest instance;
    public InternalUserRegisterRequest2UserRegisterRequest(InternalUserRegisterRequest request) {
        this.instance = new UserRegisterRequest(request.email(), request.password(), request.firstName(), request.lastName());
    }
    public UserRegisterRequest getInstance(){return this.instance;}
}
