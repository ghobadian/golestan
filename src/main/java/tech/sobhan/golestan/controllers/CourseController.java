package tech.sobhan.golestan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.services.CourseService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping(COURSE_LIST_PATH)
    private String list(@RequestHeader(value = "username") String username,
                              @RequestHeader(value = "password") String password){
        return service.list(username, password);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(COURSE_CREATE_PATH)
    private String create(@RequestParam int units, @RequestParam String title,
                          @RequestHeader(value = "username") String username,
                          @RequestHeader(value = "password") String password){
        return service.create(units, title, username, password);
    }

    @GetMapping(COURSE_READ_PATH)
    private String read(@PathVariable Long id,
                        @RequestHeader(value = "username") String username,
                        @RequestHeader(value = "password") String password){
        return service.read(id, username, password);
    }//todo add advice

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(COURSE_UPDATE_PATH)
    private String update(@PathVariable Long id,
                          @RequestParam(required = false) String title,
                          @RequestParam(required = false) Integer units,
                          @RequestHeader(value = "username") String username, 
                          @RequestHeader(value = "password") String password){
        return service.update(id, username, password, title, units);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(COURSE_DELETE_PATH)
    private String delete(@PathVariable Long id,
                                  @RequestHeader(value = "username") String username,
                                  @RequestHeader(value = "password") String password){
        return service.delete(id, username, password);
    }
}
