package internal.management.accounts.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EmailVO {
    private String value;

    public String obfuscate(){
        String[] emailParts = value.split("@");
        String obfuscate = emailParts[0];
        String emailProvider = emailParts[1];
        if (obfuscate.length() <= 3) {
            return "*".repeat(obfuscate.length()) + "@" + emailProvider;
        }
        return (obfuscate.substring(0,3))+"*".repeat(obfuscate.length() - 3)+emailProvider;
    }
}
