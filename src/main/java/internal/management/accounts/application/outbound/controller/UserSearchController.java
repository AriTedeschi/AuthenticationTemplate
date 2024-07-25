package internal.management.accounts.application.outbound.controller;


import internal.management.accounts.application.annotation.SearchOperation;
import internal.management.accounts.application.inbound.response.UserRegisterResponse;
import internal.management.accounts.application.outbound.request.UserSearchFilter;
import internal.management.accounts.config.exception.ApiErrorMessage;
import internal.management.accounts.config.exception.ValidationException;
import internal.management.accounts.domain.service.outbound.UserSearchService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(UserSearchController.BASE_URL)
public class UserSearchController {
    public static final String BASE_URL = "/users";
    private final UserSearchService service;

    public UserSearchController(UserSearchService service) {
        this.service = service;
    }

    @GetMapping("")
    @SearchOperation(
            summary = "Search Users",
            responseClass = UserRegisterResponse.class,
            errorClass = ApiErrorMessage.class
    )
    public ResponseEntity<Page<UserRegisterResponse>> register(
            @RequestParam(value = "page", defaultValue = "0") 		    Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "10")  Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "DESC") 	String 	direction,
            @RequestParam(value = "orderBy", defaultValue = "createdAt")String 	orderBy,
            @RequestParam(value = "lang", required = false)             String lang,
            //============================================================================
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "userCode", required = false) String userCode,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            HttpServletRequest request){
        validatePagination(page, linesPerPage, direction);
        direction = direction.toUpperCase();
        UserSearchFilter filter = new UserSearchFilter(userId, userCode, email, firstName, lastName, lang);

        Pageable pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        return ResponseEntity.ok(service.search(filter,pageRequest));
    }

    @GetMapping("/{identifier}")
    @SearchOperation(
            summary = "Get user by identifier",
            responseClass = UserRegisterResponse.class,
            errorClass = ApiErrorMessage.class
    )
    public ResponseEntity<UserRegisterResponse> getBy(
            @RequestParam(value = "lang", required = false) String lang,
            @PathParam("identifier") String identifier,
            HttpServletRequest request){
        return ResponseEntity.ok(service.getBy(identifier, lang));
    }

    private void validatePagination(Integer page, Integer linesPerPage, String direction){
        Map<String,String> errors = new HashMap<>();
        if(page <= -1)
            errors.put("page","Invalid pageNumber");
        if(linesPerPage <= 0)
            errors.put("linesPerPage","Invalid linesPerPage");
        if(!direction.equalsIgnoreCase("DESC") && !direction.equalsIgnoreCase("ASC"))
            errors.put("direction","Invalid linesPerPage");

        Optional.ofNullable(errors).map(m -> {
           if(m != null && !m.isEmpty())
               throw new ValidationException(m);
           return m;
        });
    }
}
