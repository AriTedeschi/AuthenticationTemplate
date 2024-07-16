package internal.management.accounts.domain.model;

import internal.management.accounts.domain.model.vo.EmailVO;
import internal.management.accounts.domain.model.vo.NameVO;
import internal.management.accounts.domain.model.vo.PasswordVO;
import internal.management.accounts.domain.model.vo.UserCode;
import internal.management.accounts.domain.model.vo.converter.UUIDv7Generator;
import internal.management.accounts.domain.model.vo.converter.UserCodeConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
public class UserEntity {
    @Id
    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @Convert(converter = UserCodeConverter.class)
    private UserCode userCode;

    @Embedded
    @AttributeOverride(name = "firstName", column = @Column(name = "first_name"))
    @AttributeOverride(name = "lastName", column = @Column(name = "last_name"))
    private NameVO fullname;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private EmailVO email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password"))
    private PasswordVO password;

    @PrePersist
    public void generateId() {
        if (uuid == null)
            uuid = UUIDv7Generator.generateUUID();
    }
}
