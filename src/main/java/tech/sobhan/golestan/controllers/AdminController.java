package tech.sobhan.golestan.controllers;

import lombok.SneakyThrows;
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

    @SneakyThrows
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(MODIFY_ROLE_PATH)
    public void modifyRole(@PathVariable Long id, @RequestBody Map<String,String> requestedBody){
        service.modifyRole(id, requestedBody);
    }
}
