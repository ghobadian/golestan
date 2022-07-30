package tech.sobhan.golestan.controllers;

import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.services.CourseSectionService;

import java.util.List;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class CourseSectionController {
    private final CourseSectionService service;

    public CourseSectionController(CourseSectionService service) {
        this.service = service;
    }

    @GetMapping(COURSE_SECTION_LIST_PATH)
    private List<CourseSection> list(@RequestParam Long termId){
        return service.list(termId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(COURSE_SECTION_CREATE_PATH)
    private CourseSection create(@RequestParam Course course, @RequestParam Instructor instructor,
                                 @RequestParam Term term){//todo check, it is so sus
        return service.create(course, instructor, term);
    }

    @SneakyThrows
    @GetMapping(COURSE_SECTION_READ_PATH)
    private JSONObject read(@PathVariable Long id){
        return service.read(id);
    }//todo add advice

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(COURSE_SECTION_UPDATE_PATH)
    private void update(@RequestBody CourseSection newCourseSection, @PathVariable Long id){
        service.update(newCourseSection, id);
    }

    @SneakyThrows
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(COURSE_SECTION_DELETE_PATH)
    private void delete(@PathVariable Long id){
        service.delete(id);
    }
}