package tech.sobhan.golestan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.services.UserService;

import java.util.List;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(USER_LIST_PATH)
    private List<User> list(){//todo if not active don't let user
        return service.list();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(USER_CREATE_PATH)
    private User create(@RequestParam String username, @RequestParam String password, @RequestParam String name,
                        @RequestParam String phone, @RequestParam String nationalId){
        return service.create(username, password, name, phone, nationalId);
    }

    @GetMapping(USER_READ_PATH)
    private User read(@PathVariable Long id){
        return service.read(id);
    }//todo add advice
}
