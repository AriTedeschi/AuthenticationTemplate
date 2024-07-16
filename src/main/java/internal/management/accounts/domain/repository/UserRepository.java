package internal.management.accounts.domain.repository;

import internal.management.accounts.domain.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Procedure(name = "generate_usercode")
    String generateUserCode();

    @Query(value = "SELECT * FROM TB_USER u WHERE u.email = ?1",
            nativeQuery = true)
    Optional<UserEntity> findByEmail(String email);
}
