package internal.management.accounts.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Embeddable
public class PasswordVO {
    private String value;

    public String obfuscate(){
        return (this.value.substring(0,4))+"*".repeat(this.value.length() - 4);
    }
}
