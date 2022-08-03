package tech.sobhan.golestan.controllers;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.services.CourseSectionService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class CourseSectionController {
    private final CourseSectionService service;

    public CourseSectionController(CourseSectionService service) {
        this.service = service;
    }

    @GetMapping(COURSE_SECTION_LIST_PATH)
    private String list(@RequestParam Long termId,
                        @RequestHeader(value = "username") String username,
                        @RequestHeader(value = "password") String password,
                        @RequestParam(required = false) String instructorName,
                        @RequestParam(required = false) Integer pageNumber,
                        @RequestParam(required = false) Integer maxInEachPage,
                        @RequestParam(required = false) String courseName){
        return service.list(termId, username, password, instructorName, courseName, pageNumber, maxInEachPage);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(COURSE_SECTION_CREATE_PATH)
    private String create(@RequestParam Long courseId,
                          @RequestParam Long instructorId,
                          @RequestParam Long termId,
                          @RequestHeader(value = "username") String username,
                          @RequestHeader(value = "password") String password){
        return service.create(courseId, instructorId, termId, username, password);
    }

    @SneakyThrows
    @GetMapping(COURSE_SECTION_READ_PATH)
    private String read(@PathVariable Long id,
                            @RequestHeader(value = "username") String username,
                            @RequestHeader(value = "password") String password){
        return service.read(id, username, password);
    }//todo add advice

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(COURSE_SECTION_UPDATE_PATH)
    private String update(@RequestParam(required = false) Long termId,
                          @RequestParam(required = false) Long courseId,
                          @RequestParam(required = false) Long instructorId,
                          @PathVariable Long courseSectionId,
                          @RequestHeader(value = "username") String username,
                          @RequestHeader(value = "password") String password){
        return service.update(termId, courseId, instructorId, courseSectionId, username, password);
    }

    @SneakyThrows
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(COURSE_SECTION_DELETE_PATH)
    private String delete(@PathVariable Long id,
                        @RequestHeader(value = "username") String username,
                        @RequestHeader(value = "password") String password){
        return service.delete(id, username, password);
    }
}