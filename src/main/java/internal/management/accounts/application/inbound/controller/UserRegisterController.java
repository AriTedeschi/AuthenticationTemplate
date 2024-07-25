package internal.management.accounts.application.inbound.controller;


import internal.management.accounts.application.annotation.RegisterOperation;
import internal.management.accounts.application.inbound.request.InternalUserRegisterRequest;
import internal.management.accounts.application.inbound.request.UserRegisterRequest;
import internal.management.accounts.application.inbound.response.UserRegisterResponse;
import internal.management.accounts.config.exception.ApiErrorMessage;
import internal.management.accounts.domain.service.inbound.UserRegisterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping(UserRegisterController.BASE_URL)
public class UserRegisterController {
    public static final String BASE_URL = "/users";
    private final UserRegisterService service;

    public UserRegisterController(UserRegisterService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @RegisterOperation(
            summary = "Register User",
            responseClass = UserRegisterResponse.class,
            errorClass = ApiErrorMessage.class
    )
    public ResponseEntity<UserRegisterResponse> register(
            @RequestBody UserRegisterRequest userRequest,
            @RequestParam(value = "lang", required = false) String lang,
            HttpServletRequest request){
        UserRegisterResponse response = service.register(userRequest,lang);
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .path("/{accountCode}")
                .buildAndExpand(response.userCode())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }


    @PostMapping("/add")
    @RegisterOperation(
            summary = "Add Internal User",
            responseClass = InternalUserRegisterRequest.class,
            errorClass = ApiErrorMessage.class
    )
    public ResponseEntity<UserRegisterResponse> add(
            @RequestBody InternalUserRegisterRequest userRequest,
            @RequestParam(value = "lang", required = false) String lang,
            HttpServletRequest request){
        UserRegisterResponse response = service.addInternalUser(userRequest,lang);
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .path("/{accountCode}")
                .buildAndExpand(response.userCode())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
