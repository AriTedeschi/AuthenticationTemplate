package internal.management.accounts.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCode {
    private String accountPrefix;
    private String accountInfix;
    private String accountSufix;

    public String get(){
        return this.accountPrefix+"-"+accountInfix+"-"+accountSufix;
    }

    public String obfuscate(){
        return (this.accountPrefix.substring(0,2))+"***-"+accountInfix+"-"+accountSufix;
    }
}
