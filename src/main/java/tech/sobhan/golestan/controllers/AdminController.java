package tech.sobhan.golestan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.services.AdminService;

import java.util.Map;

import static tech.sobhan.golestan.constants.ApiPaths.MODIFY_ROLE_PATH;

@RestController
public class AdminController {
    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(MODIFY_ROLE_PATH)
    public String modifyRole(@PathVariable Long id,
                             @RequestBody Map<String,String> requestedBody,
                             @RequestHeader(value = "username") String username,
                             @RequestHeader(value = "password") String password){
        return service.modifyRole(id, requestedBody, username, password);
    }
}
