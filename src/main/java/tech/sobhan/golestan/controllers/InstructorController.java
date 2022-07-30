package tech.sobhan.golestan.controllers;

import lombok.extern.slf4j.Slf4j;
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
    private List<Instructor> list(){//todo send also the user
        return service.list();
    }

    @GetMapping(INSTRUCTOR_READ_PATH)
    private Instructor read(@PathVariable Long id){
        return service.read(id);
    }//todo add advice

    @PostMapping(GIVE_MARK_PATH)
    public void giveMark(@RequestBody Map<String, String> json, @PathVariable Long instructor_id){//todo test
        service.giveMark(json);
    }
}
