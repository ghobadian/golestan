package tech.sobhan.golestan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.services.CourseService;

import java.util.List;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping(COURSE_LIST_PATH)
    private List<Course> list(){
        return service.list();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(COURSE_CREATE_PATH)
    private Course create(@RequestParam int units, @RequestParam String title){
        return service.create(units, title);
    }

    @GetMapping(COURSE_READ_PATH)
    private Course read(@PathVariable Long id){
        return service.read(id);
    }//todo add advice

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(COURSE_UPDATE_PATH)
    private void update(@RequestBody Course newCourse, @PathVariable Long id){
        service.update(newCourse, id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(COURSE_DELETE_PATH)
    private void delete(@PathVariable Long id){
        service.delete(id);
    }
}
