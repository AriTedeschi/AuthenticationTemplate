package internal.management.accounts.application.outbound.controller;

import internal.management.accounts.application.annotation.AuthOperation;
import internal.management.accounts.config.exception.ApiErrorMessage;
import internal.management.accounts.config.security.TokenService;
import internal.management.accounts.application.outbound.request.UserLogin;
import internal.management.accounts.application.outbound.response.LoginResponse;
import internal.management.accounts.domain.model.UserAuthenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    @AuthOperation(
            summary = "Authenticate",
            responseClass = LoginResponse.class,
            errorClass = ApiErrorMessage.class
    )
    public ResponseEntity login (UserLogin login){
        var usernamePassword = new UsernamePasswordAuthenticationToken(login.login(),login.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserAuthenticated) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
