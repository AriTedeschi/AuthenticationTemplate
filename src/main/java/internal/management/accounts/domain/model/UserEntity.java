package internal.management.accounts.domain.model;

import internal.management.accounts.domain.model.vo.EmailVO;
import internal.management.accounts.domain.model.vo.NameVO;
import internal.management.accounts.domain.model.vo.PasswordVO;
import internal.management.accounts.domain.model.vo.UserCode;
import internal.management.accounts.domain.model.vo.converter.UserCodeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_USER")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Convert(converter = UserCodeConverter.class)
    @Column(name = "user_code")
    private UserCode userCode;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private EmailVO email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private PasswordVO password;

    @Embedded
    @AttributeOverride(name = "firstName", column = @Column(name = "first_name"))
    @AttributeOverride(name = "lastName", column = @Column(name = "last_name"))
    private NameVO fullname;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
