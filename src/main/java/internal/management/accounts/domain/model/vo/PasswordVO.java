package internal.management.accounts.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PasswordVO {
    private String value;

    public String obfuscate(){
        return (this.value.substring(0,4))+"*".repeat(this.value.length() - 4);
    }

    public PasswordVO encode(String password) {
        //TODO Encrypt password
        this.value=password;
        return this;
    }
}
