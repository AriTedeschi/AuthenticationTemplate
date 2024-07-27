package internal.management.accounts.domain.model.vo.converter;

import internal.management.accounts.domain.model.vo.UserCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserCodeConverter implements AttributeConverter<UserCode, String> {

    @Override
    public String convertToDatabaseColumn(UserCode userCode) {
        if (userCode == null) {
            return null;
        }
        return userCode.getAccountPrefix() + "-" + userCode.getAccountInfix()+ "-" + userCode.getAccountSufix();
    }

    @Override
    public UserCode convertToEntityAttribute(String dbData) {
        String[] parts = slice(dbData);
        return new UserCode(parts[0], parts[1], parts[2]);
    }

    public static String[] slice(String dbData){
        if (dbData == null || dbData.isEmpty()) {
            return new String[]{};
        }
        String[] parts = dbData.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid user code format");
        }
        return parts;
    }

    public static boolean isUserCode(String login){
        long hyphenCount = login.chars().filter(ch -> ch == '-').count();
        return login.length() == 17 && hyphenCount == 2;
    }

    public static Integer getRole(String login){
        return isUserCode(login) ? Integer.valueOf(login.split("-")[1]) : null;
    }
}

