package tech.sobhan.golestan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.services.UserService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(USER_LIST_PATH)
    private String list(@RequestHeader(value = "username") String username,
                            @RequestHeader(value = "password") String password){
        return service.list(username, password);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(USER_CREATE_PATH)
    private User create(@RequestParam String username, @RequestParam String password, @RequestParam String name,
                        @RequestParam String phone, @RequestParam String nationalId){
        return service.create(username, password, name, phone, nationalId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(USER_READ_PATH)
    private String read(@PathVariable Long id,
                      @RequestHeader(value = "username") String username,
                      @RequestHeader(value = "password") String password){
        return service.read(id, username, password);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(USER_UPDATE_PATH)
    private String update(@RequestParam(required = false) String name,
                          @RequestParam(required = false) String newUsername,
                          @RequestParam(required = false) String newPassword,
                          @RequestParam(required = false) String phone,
                          @RequestHeader(value = "username") String username,
                          @RequestHeader(value = "password") String password){
        return service.update(name, newUsername, newPassword, phone , username, password);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(USER_DELETE_PATH)
    private void delete(@PathVariable Long id,
                          @RequestHeader(value = "username") String username,
                          @RequestHeader(value = "password") String password){
        service.delete(id, username, password);
    }
}
