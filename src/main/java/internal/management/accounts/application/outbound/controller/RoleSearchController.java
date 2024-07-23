package internal.management.accounts.application.outbound.controller;


import internal.management.accounts.application.annotation.SearchOperation;
import internal.management.accounts.application.inbound.request.RoleRegisterRequest;
import internal.management.accounts.application.outbound.request.RoleSearchFilter;
import internal.management.accounts.config.exception.ApiErrorMessage;
import internal.management.accounts.config.exception.ValidationException;
import internal.management.accounts.domain.service.outbound.RoleSearchService;
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
@RequestMapping(RoleSearchController.BASE_URL)
public class RoleSearchController {
    public static final String BASE_URL = "/roles";
    private final RoleSearchService service;

    public RoleSearchController(RoleSearchService service) {
        this.service = service;
    }

    @GetMapping("")
    @SearchOperation(
            summary = "Search Users",
            responseClass = RoleRegisterRequest.class,
            errorClass = ApiErrorMessage.class
    )
    public ResponseEntity<Page<RoleRegisterRequest>> register(
            @RequestParam(value = "page", defaultValue = "0") 		    Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "10")  Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "DESC") 	String 	direction,
            @RequestParam(value = "orderBy", defaultValue = "id")       String 	orderBy,
            @RequestParam(value = "lang", required = false)             String lang,
            //============================================================================
            @RequestParam(value = "roleId", required = false) String roleId,
            @RequestParam(value = "roleName", required = false) String roleName,
            HttpServletRequest request){
        validatePagination(page, linesPerPage, direction);
        direction = direction.toUpperCase();
        RoleSearchFilter filter = new RoleSearchFilter(roleId, roleName, lang);

        Pageable pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        return ResponseEntity.ok(service.search(filter,pageRequest));
    }


    @GetMapping("/{id}")
    @SearchOperation(
            summary = "Search role by id",
            responseClass = RoleRegisterRequest.class,
            errorClass = ApiErrorMessage.class
    )
    public ResponseEntity<RoleRegisterRequest> byId(
            @RequestParam(value = "lang", required = false) String lang,
            @PathParam("id") Integer id){
        return ResponseEntity.ok(service.byId(id,lang));
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
