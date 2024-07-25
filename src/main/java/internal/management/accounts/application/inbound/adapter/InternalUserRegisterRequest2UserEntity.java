package internal.management.accounts.application.inbound.adapter;

import internal.management.accounts.application.inbound.request.InternalUserRegisterRequest;
import internal.management.accounts.domain.model.UserEntity;
import internal.management.accounts.domain.model.vo.EmailVO;
import internal.management.accounts.domain.model.vo.NameVO;
import internal.management.accounts.domain.model.vo.PasswordVO;
import internal.management.accounts.domain.model.vo.UserCode;

import java.time.LocalDateTime;

public class InternalUserRegisterRequest2UserEntity {
    private UserEntity instance;
    public InternalUserRegisterRequest2UserEntity(InternalUserRegisterRequest request) {
        this.instance = UserEntity.builder()
                .userCode(new UserCode())
                .email(new EmailVO(request.email()))
                .password(new PasswordVO().encode(request.password()))
                .fullname(new NameVO(request.firstName(), request.lastName()))
                .createdAt(LocalDateTime.now())
                .build();
    }
    public UserEntity getInstance(){return this.instance;}
}
