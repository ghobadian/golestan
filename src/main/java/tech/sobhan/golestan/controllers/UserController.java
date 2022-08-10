package tech.sobhan.golestan.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.services.security.UserSecurityService;

import java.util.List;
import java.util.Map;

import static tech.sobhan.golestan.constants.ApiPaths.*;
import static tech.sobhan.golestan.documentation.UserController.GIVE_ROLE_SCHEMA;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserSecurityService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(USER_LIST_PATH)
    private List<User> list(@RequestHeader String token) {
        return service.list(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(USER_CREATE_PATH)
    private User create(@RequestParam String username, @RequestParam String password, @RequestParam String name,
                        @RequestParam String phone, @RequestParam String nationalId) {
        return service.create(username, password, name, phone, nationalId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(USER_READ_PATH)
    private String read(@PathVariable Long id, @RequestHeader String token) {
        return service.read(id, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(USER_UPDATE_PATH)
    private String update(@RequestParam(required = false) String name,
                          @RequestParam(required = false) String newUsername,
                          @RequestParam(required = false) String newPassword,
                          @RequestParam(required = false) String phone,
                          @RequestHeader String token) {
        return service.update(name, newUsername, newPassword, phone , token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(USER_DELETE_PATH)
    private void delete(@PathVariable Long id, @RequestHeader String token) {
        service.delete(id, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(MODIFY_ROLE_PATH)
    public User modifyRole(@PathVariable Long id,
                             @Parameter(schema = @Schema(example = GIVE_ROLE_SCHEMA)) @RequestBody Map<String,String> requestedBody,
                             @RequestHeader String token) {
        return service.modifyRole(id, requestedBody, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(LOGIN_PATH)
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        return service.login(username, password);
        //throw error if current user is logging in with the same user
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(LOGOUT_PATH)
    public String logout(@RequestHeader String token) {
        return service.logout(token);
        //throw error if current user is logging in with the same user
    }
}
