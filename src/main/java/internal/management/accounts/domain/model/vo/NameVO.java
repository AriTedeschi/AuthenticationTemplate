package internal.management.accounts.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NameVO {
    private String firstName;
    private String lastName;
    public String get(){return firstName + " " +lastName;}

    public String obfuscate(){
        String obfuscated = "";
        String fullname = get();
        String[] nameTokens = fullname.split(" ");
        for(int i = 0; i < nameTokens.length; i++){
            obfuscated = i % 2 == 0 ? ("*".repeat(nameTokens[i].length())) : nameTokens[i];
        }
        return obfuscated;
    }
}
