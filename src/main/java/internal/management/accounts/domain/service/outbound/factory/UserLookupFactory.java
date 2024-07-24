package internal.management.accounts.domain.service.outbound.factory;

import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class UserLookupFactory {

    public static UserEntity getBy(String login, UserRepository repository){
        long hyphenCount = login.chars().filter(ch -> ch == '-').count();
        UserLookup findByUuid = (userCode) -> repository.findByUuid(userCode);
        UserLookup findByUserCode = (userCode) -> repository.findByUserCode(userCode);
        UserLookup findByEmail = (email) -> repository.findByEmail(email);

        if (login.length() == 36 && hyphenCount == 4){
            log.info("Getting by uuid");
            return findByUuid.apply(login).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        }

        if (login.length() == 17 && hyphenCount == 2){
            log.info("Getting by UserCode");
            return findByUserCode.apply(login).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        }

        log.info("Getting by email");
        return findByEmail.apply(login).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
