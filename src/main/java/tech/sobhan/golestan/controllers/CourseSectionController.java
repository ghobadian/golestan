package tech.sobhan.golestan.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.dto.CourseSectionDTO;
import tech.sobhan.golestan.models.dto.StudentDTO;
import tech.sobhan.golestan.services.CourseSectionService;
import tech.sobhan.golestan.services.security.CourseSectionSecurityService;

import java.util.List;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
@RequiredArgsConstructor
public class CourseSectionController {
    private final CourseSectionService service;
    private final CourseSectionSecurityService securityService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(COURSE_SECTION_LIST_PATH)
    private List<CourseSection> list(@RequestParam Long termId,
                                     @RequestHeader String token,
                                     @RequestParam(required = false) String instructorName,
                                     @RequestParam(required = false) Integer pageNumber,
                                     @RequestParam(required = false) Integer maxInEachPage,
                                     @RequestParam(required = false) String courseName) {
        securityService.list(token);
        return service.list(termId, instructorName, courseName, pageNumber, maxInEachPage);
    }

    @GetMapping(LIST_COURSE_SECTION_STUDENTS_PATH)
    public List<StudentDTO> listStudentsOfSpecifiedCourseSection(@RequestParam Long courseSectionId,
                                                                 @RequestHeader String token) {
        securityService.listCourseSectionStudents(courseSectionId, token);
        return service.listCourseSectionStudents(courseSectionId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(COURSE_SECTION_CREATE_PATH)
    private CourseSection create(@RequestParam Long courseId,
                          @RequestParam Long instructorId,
                          @RequestParam Long termId,
                          @RequestHeader String token) {
        securityService.create(courseId, instructorId, termId, token);
        return service.create(courseId, instructorId, termId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(COURSE_SECTION_READ_PATH)
    private CourseSectionDTO read(@PathVariable Long id, @RequestHeader String token) {
        securityService.read(token);
        return service.read(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(COURSE_SECTION_UPDATE_PATH)
    private CourseSection update(@RequestParam(required = false) Long termId,
                                 @RequestParam(required = false) Long courseId,
                                 @RequestParam(required = false) Long instructorId,
                                 @PathVariable Long courseSectionId,
                                 @RequestHeader String token) {
        securityService.update(token, courseSectionId);
        return service.update(termId, courseId, instructorId, courseSectionId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(COURSE_SECTION_DELETE_PATH)
    private void delete(@PathVariable Long id,
                        @RequestHeader String token) {
        securityService.delete(id, token);
        service.delete(id);
    }
}