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
        accountSufix = parts[3];
    }

    public String get(){
        return this.accountPrefix+"-"+accountInfix+"-"+accountSufix;
    }

    public String obfuscate(){
        return (this.accountPrefix.substring(0,2))+"***-"+accountInfix+"-"+accountSufix;
    }
}
