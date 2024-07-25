package internal.management.accounts.domain.model.vo;

import internal.management.accounts.domain.model.vo.converter.UserCodeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCode {
    private String accountPrefix;
    private String accountInfix;
    private String accountSufix;

    public void set(String userCode) {
        String[] parts = UserCodeConverter.slice(userCode);
        accountPrefix = parts[0];
        accountInfix = parts[1];
        accountSufix = parts[2];
    }

    public void setFromUsercode(String userCodePrefix, String role, String subUserId) {
        accountPrefix = userCodePrefix;
        accountInfix = role;
        accountSufix = subUserId;
    }

    public String get(){
        return this.accountPrefix+"-"+accountInfix+"-"+accountSufix;
    }

    public String obfuscate(){
        return (this.accountPrefix.substring(0,2))+"**"+this.accountPrefix.substring(4,5)+"-"+accountInfix+"-"+accountSufix;
    }

    public String getRole(){
        return accountInfix;
    }
}
