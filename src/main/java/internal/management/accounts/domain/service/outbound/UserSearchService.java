package internal.management.accounts.domain.service.outbound;

import internal.management.accounts.application.inbound.response.UserRegisterResponse;
import internal.management.accounts.application.outbound.request.UserSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserSearchService {
    Page<UserRegisterResponse> search(UserSearchFilter request, Pageable pageable);
    UserRegisterResponse getBy(String identifier, String lang);
}
