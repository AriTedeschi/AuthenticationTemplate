package internal.management.accounts.domain.service.outbound.factory;

import internal.management.accounts.domain.model.UserEntity;

import java.util.Optional;

@FunctionalInterface
public interface UserLookup {
    Optional<UserEntity> apply(String login);
}
