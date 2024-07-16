package internal.management.accounts.domain.model.adapter;

import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.model.response.UserRegisterResponse;

public class UserEntity2UserRegisterResponse {
    private UserRegisterResponse instance;
    public UserEntity2UserRegisterResponse(UserEntity entity, boolean obfuscate) {
        this.instance = obfuscate ? convertObfuscated(entity) : convert(entity);
    }

    private UserRegisterResponse convert(UserEntity entity){
        return new UserRegisterResponse(entity.getUuid().toString(),
                entity.getUserCode().get(),
                entity.getEmail().getValue(),
                entity.getFullname().get());
    }

    private UserRegisterResponse convertObfuscated(UserEntity entity){
        return new UserRegisterResponse(entity.getUuid().toString(),
                entity.getUserCode().obfuscate(),
                entity.getEmail().obfuscate(),
                entity.getFullname().obfuscate());
    }

    public UserRegisterResponse getInstance(){return this.instance;}
}
