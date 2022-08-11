package tech.sobhan.golestan.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.services.CourseService;
import tech.sobhan.golestan.services.security.CourseSecurityService;

import java.util.List;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseSecurityService securityService;
    private final CourseService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(COURSE_LIST_PATH)
    private List<Course> list(@RequestHeader String token) {
        securityService.list(token);
        return service.list();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(COURSE_CREATE_PATH)
    private Course create(@RequestParam int units, @RequestParam String title, @RequestHeader String token) {
        securityService.create(title, token);
        return service.create(units, title);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(COURSE_READ_PATH)
    private Course read(@PathVariable Long id, @RequestHeader String token) {
        securityService.read(token);
        return service.read(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(COURSE_UPDATE_PATH)
    private Course update(@PathVariable Long id,
                          @RequestParam(required = false) String title,
                          @RequestParam(required = false) Integer units,
                          @RequestHeader String token) {
        securityService.update(token);
        return service.update(id, title, units);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(COURSE_DELETE_PATH)
    private void delete(@PathVariable Long id, @RequestHeader String token) {
        securityService.delete(token);
        service.delete(id);
    }
}
