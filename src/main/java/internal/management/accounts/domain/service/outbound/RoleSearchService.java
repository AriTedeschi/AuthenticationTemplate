package internal.management.accounts.domain.service.outbound;

import internal.management.accounts.application.inbound.request.RoleRegisterRequest;
import internal.management.accounts.application.outbound.request.RoleSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleSearchService {
    Page<RoleRegisterRequest> search(RoleSearchFilter request, Pageable pageable);

    RoleRegisterRequest byId(Integer id, String lang);
}
