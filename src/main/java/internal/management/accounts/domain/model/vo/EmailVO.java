package internal.management.accounts.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Embeddable
public class EmailVO {
    private String value;

    public String obfuscate(){
        String obfuscate = value.split("@")[0];
        String emailProvider = value.split("@")[1];
        return (obfuscate.substring(0,3))+"*".repeat(obfuscate.length() - 3)+emailProvider;
    }
}
