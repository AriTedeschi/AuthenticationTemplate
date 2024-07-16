package internal.management.accounts.domain.model.adapter;

import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.model.request.UserRegisterRequest;
import internal.management.accounts.domain.model.vo.EmailVO;
import internal.management.accounts.domain.model.vo.NameVO;
import internal.management.accounts.domain.model.vo.PasswordVO;
import internal.management.accounts.domain.model.vo.UserCode;

public class UserRegisterRequest2UserEntity {
    private UserEntity instance;
    public UserRegisterRequest2UserEntity(UserRegisterRequest request) {
        this.instance = UserEntity.builder()
                .userCode(new UserCode())
                .email(new EmailVO(request.email()))
                .password(new PasswordVO().encode(request.password()))
                .fullname(new NameVO(request.firstName(), request.lastName()))
                .build();
    }
    public UserEntity getInstance(){return this.instance;}
}
