package internal.management.accounts.domain.service.outbound;

import internal.management.accounts.domain.model.UserAuthenticated;
import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.repository.UserRepository;
import internal.management.accounts.domain.service.outbound.factory.UserLookupFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity userEntity = UserLookupFactory.getBy(login, repository);
        return new UserAuthenticated(userEntity);
    }
}