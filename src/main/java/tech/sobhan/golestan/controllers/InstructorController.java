package tech.sobhan.golestan.controllers;

import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.services.InstructorService;

import java.util.List;
import java.util.Map;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class InstructorController {
    private final InstructorService service;

    public InstructorController(InstructorService service) {
        this.service = service;
    }

    @GetMapping(INSTRUCTOR_LIST_PATH)
    private String list(@RequestHeader(value = "username") String username,
                                  @RequestHeader(value = "password") String password){//todo send also the user
        return service.list(username, password);
    }

    @GetMapping(INSTRUCTOR_READ_PATH)
    private String read(@PathVariable Long id,
                            @RequestHeader(value = "username") String username,
                            @RequestHeader(value = "password") String password){
        return service.read(id, username, password);
    }//todo add advice

    @PostMapping(GIVE_MARK_PATH)
    public String giveMark(@RequestBody Map<String, String> json,
                         @RequestHeader(value = "username") String username,
                         @RequestHeader(value = "password") String password){//todo test
        return service.giveMark(json, username, password);
    }
}
