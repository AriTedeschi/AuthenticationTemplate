package internal.management.accounts.application.inbound.controller;


import internal.management.accounts.application.annotation.RegisterOperation;
import internal.management.accounts.application.inbound.request.RoleRegisterRequest;
import internal.management.accounts.config.exception.ApiErrorMessage;
import internal.management.accounts.domain.service.inbound.RoleRegisterService;
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
@RequestMapping(RoleController.BASE_URL)
public class RoleController {
    public static final String BASE_URL = "/roles";
    private final RoleRegisterService service;

    public RoleController(RoleRegisterService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @RegisterOperation(
            summary = "Register Role",
            responseClass = RoleRegisterRequest.class,
            errorClass = ApiErrorMessage.class
    )
    public ResponseEntity<RoleRegisterRequest> register(
            @RequestBody RoleRegisterRequest roleRegister,
            @RequestParam(value = "lang", required = false) String lang,
            HttpServletRequest request){
        RoleRegisterRequest response = service.addMember(roleRegister,lang);
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .path("/{accountCode}")
                .buildAndExpand(response.roleId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
