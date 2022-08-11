package tech.sobhan.golestan.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.services.UserService;
import tech.sobhan.golestan.services.security.UserSecurityService;

import java.util.List;
import java.util.Map;

import static tech.sobhan.golestan.constants.ApiPaths.*;
import static tech.sobhan.golestan.documentation.UserController.GIVE_ROLE_SCHEMA;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserSecurityService securityService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(USER_LIST_PATH)
    private List<User> list(@RequestHeader String token) {
        securityService.list(token);
        return service.list();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(USER_CREATE_PATH)
    private User create(@RequestParam String username, @RequestParam String password, @RequestParam String name,
                        @RequestParam String phone, @RequestParam String nationalId) {
        securityService.create(username, phone, nationalId);
        return service.create(username, password, name, phone, nationalId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(USER_READ_PATH)
    private User read(@PathVariable Long id, @RequestHeader String token) {
        securityService.read(token);
        return service.read(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(USER_UPDATE_PATH)
    private User update(@RequestParam(required = false) String name,
                          @RequestParam(required = false) String newUsername,
                          @RequestParam(required = false) String newPassword,
                          @RequestParam(required = false) String phone,
                          @RequestHeader String token) {
        securityService.update(token);
        return service.update(name, newUsername, newPassword, phone , token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(USER_DELETE_PATH)
    private void delete(@PathVariable Long id, @RequestHeader String token) {
        securityService.delete(token);
        service.delete(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(MODIFY_ROLE_PATH)
    public User modifyRole(@PathVariable Long id,
                             @Parameter(schema = @Schema(example = GIVE_ROLE_SCHEMA)) @RequestBody Map<String,String> requestedBody,
                             @RequestHeader String token) {
        securityService.modifyRole(token);
        return service.modifyRole(id, requestedBody);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(LOGIN_PATH)
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        securityService.login(username, password);
        return service.login(username);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(LOGOUT_PATH)
    public void logout(@RequestHeader String token) {
        securityService.logout(token);
        service.logout(token);
    }
}
