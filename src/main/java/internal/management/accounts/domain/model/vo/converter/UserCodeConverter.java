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
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        String[] parts = dbData.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid user code format");
        }
        return new UserCode(parts[0], parts[1], parts[2]);
    }
}

