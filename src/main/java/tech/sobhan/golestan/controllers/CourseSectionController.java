package tech.sobhan.golestan.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.services.security.CourseSectionSecurityService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
@RequiredArgsConstructor
public class CourseSectionController {
    private final CourseSectionSecurityService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(COURSE_SECTION_LIST_PATH)
    private String list(@RequestParam Long termId,
                        @RequestHeader String token,
                        @RequestParam(required = false) String instructorName,
                        @RequestParam(required = false) Integer pageNumber,
                        @RequestParam(required = false) Integer maxInEachPage,
                        @RequestParam(required = false) String courseName){
        return service.list(termId, token, instructorName, courseName, pageNumber, maxInEachPage);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(COURSE_SECTION_CREATE_PATH)
    private String create(@RequestParam Long courseId,
                          @RequestParam Long instructorId,
                          @RequestParam Long termId,
                          @RequestHeader String token){
        return service.create(courseId, instructorId, termId, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(COURSE_SECTION_READ_PATH)
    private String read(@PathVariable Long id, @RequestHeader String token){
        return service.read(id, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(COURSE_SECTION_UPDATE_PATH)
    private String update(@RequestParam(required = false) Long termId,
                          @RequestParam(required = false) Long courseId,
                          @RequestParam(required = false) Long instructorId,
                          @PathVariable Long courseSectionId,
                          @RequestHeader String token){
        return service.update(termId, courseId, instructorId, courseSectionId, token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(COURSE_SECTION_DELETE_PATH)
    private void delete(@PathVariable Long id,
                        @RequestHeader String token){
        service.delete(id, token);
    }
}