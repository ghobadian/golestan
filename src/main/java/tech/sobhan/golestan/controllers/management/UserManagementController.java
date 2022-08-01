package tech.sobhan.golestan.controllers.management;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.services.UserService;

import static tech.sobhan.golestan.constants.ApiPaths.USER_DELETE_PATH;
import static tech.sobhan.golestan.constants.ApiPaths.USER_UPDATE_PATH;

public class UserManagementController {
    private final UserService service;

    public UserManagementController(UserService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(USER_UPDATE_PATH)
    private String update(@RequestBody User newUser, @PathVariable Long id,
                          @RequestHeader(value = "username") String username,
                          @RequestHeader(value = "password") String password){
        return service.update(newUser, id, username, password);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(USER_DELETE_PATH)
    private String delete(@PathVariable Long id,
                        @RequestHeader(value = "username") String username,
                        @RequestHeader(value = "password") String password){
        return service.delete(id, username, password);
    }
}
