package tech.sobhan.golestan.controllers;

import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.services.security.StudentSecurityService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class StudentController {
    private final StudentSecurityService service;

    public StudentController(StudentSecurityService service) {
        this.service = service;
    }

    @GetMapping(LIST_COURSE_SECTION_STUDENTS_PATH)//todo move to studentController
    public String listStudentsOfSpecifiedCourseSection(@RequestParam Long courseSectionId,
                                                       @RequestHeader(value = "username") String username,
                                                       @RequestHeader(value = "password") String password){
        return service.listCourseSectionStudents(courseSectionId, username, password);
    }
    @PostMapping(SIGNUP_SECTION_PATH)
    public String signUpSection(@RequestParam Long courseSectionId,
                              @RequestHeader(value = "username") String username,
                              @RequestHeader(value = "password") String password){
        return service.signUpSection(courseSectionId, username, password);
    }

    @GetMapping(SEE_SCORES_IN_TERM_PATH)
    public String seeScoresInSpecifiedTerm(@RequestParam Long termId,
                                              @RequestHeader(value = "username") String username,
                                              @RequestHeader(value = "password") String password){
        return service.seeScoresInSpecifiedTerm(termId, username, password).toString();
    }

    @GetMapping(SEE_SUMMERY_PATH)
    public String seeSummery(@RequestHeader(value = "username") String username,
                                @RequestHeader(value = "password") String password){
        return service.seeSummery(username, password);
    }
}
