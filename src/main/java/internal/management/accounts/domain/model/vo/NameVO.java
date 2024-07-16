package internal.management.accounts.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
public class NameVO {
    private String firstName;
    private String lastName;

    public String obfuscate(){
        String obfuscated = "";
        String fullname = firstName + " " +lastName;
        String[] nameTokens = fullname.split(" ");
        for(int i = 0; i < nameTokens.length; i++){
            obfuscated = i % 2 == 0 ? ("*".repeat(nameTokens[i].length())) : nameTokens[i];
        }
        return obfuscated;
    }
}
