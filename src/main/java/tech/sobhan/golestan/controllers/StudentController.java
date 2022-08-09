package tech.sobhan.golestan.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.services.security.StudentSecurityService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
@RequiredArgsConstructor
public class StudentController {
    private final StudentSecurityService service;

    @GetMapping(LIST_COURSE_SECTION_STUDENTS_PATH)//todo move to studentController
    public String listStudentsOfSpecifiedCourseSection(@RequestParam Long courseSectionId,
                                                       @RequestHeader String token){
        return service.listCourseSectionStudents(courseSectionId, token);
    }
    @PostMapping(SIGNUP_SECTION_PATH)
    public CourseSectionRegistration signUpSection(@RequestParam Long courseSectionId,
                                                   @RequestHeader String token){
        return service.signUpSection(courseSectionId, token);
    }

    @GetMapping(SEE_SCORES_IN_TERM_PATH)
    public String seeScoresInSpecifiedTerm(@RequestParam Long termId, @RequestHeader String token){
        return service.seeScoresInSpecifiedTerm(termId, token).toString();
    }

    @GetMapping(SEE_SUMMERY_PATH)
    public String seeSummery(@RequestHeader String token){
        return service.seeSummery(token);
    }
}
