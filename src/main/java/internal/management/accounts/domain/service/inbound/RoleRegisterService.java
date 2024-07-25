package internal.management.accounts.domain.service.inbound;

import internal.management.accounts.application.inbound.request.RoleRegisterRequest;

public interface RoleRegisterService {
    RoleRegisterRequest addMember(RoleRegisterRequest request, String locale);
}
