package internal.management.accounts.domain.repository;

import internal.management.accounts.domain.model.UserEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query(value = "SELECT generate_user_code()", nativeQuery = true)
    String generateUserCode() throws DataAccessException;

    @Query(value = "SELECT * FROM TB_USER u WHERE u.user_code = (SELECT v.user_code FROM V_USER v where v.email=?1 and v.infix_code=?2)",
            nativeQuery = true)
    Optional<UserEntity> findByEmail(String email, Integer roleId);

    @Query(value = "SELECT * FROM PUBLIC.TB_USER u WHERE u.user_code = ?1",
            nativeQuery = true)
    Optional<UserEntity> findByUserCode(String userCode);

    @Query(value = "SELECT * FROM PUBLIC.TB_USER u WHERE u.uuid = ?1",
            nativeQuery = true)
    Optional<UserEntity> findByUuid(String uuid);

    @Query(value = "SELECT max(infix_code)+1 FROM V_USER where infix_code=?1 and PREFIX=?2",
            nativeQuery = true)
    Optional<Integer> getNextSubUserId(Integer roleId, String userPrefix);
}
