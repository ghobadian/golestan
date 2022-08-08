package tech.sobhan.golestan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.services.security.CourseSecurityService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class CourseController {
    private final CourseSecurityService service;

    public CourseController(CourseSecurityService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(COURSE_LIST_PATH)
    private String list(@RequestHeader String token){
        return service.list(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(COURSE_CREATE_PATH)
    private String create(@RequestParam int units, @RequestParam String title, @RequestHeader String token){
        return service.create(units, title, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(COURSE_READ_PATH)
    private String read(@PathVariable Long id, @RequestHeader String token){
        return service.read(id, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(COURSE_UPDATE_PATH)
    private String update(@PathVariable Long id,
                          @RequestParam(required = false) String title,
                          @RequestParam(required = false) Integer units,
                          @RequestHeader String token){
        return service.update(id, token, title, units);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(COURSE_DELETE_PATH)
    private void delete(@PathVariable Long id, @RequestHeader String token){
        service.delete(id, token);
    }
}
