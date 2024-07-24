package internal.management.accounts.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class PasswordVO {
    private String value;

    public String obfuscate(){
        return (this.value.substring(0,4))+"*".repeat(this.value.length() - 4);
    }

    public PasswordVO encode(String password) {
        this.value = new BCryptPasswordEncoder().encode(password);
        return this;
    }
}
