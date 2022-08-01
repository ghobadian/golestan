package tech.sobhan.golestan.controllers;

import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.services.StudentService;


import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping(LIST_COURSE_SECTION_STUDENTS_PATH)
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
    }//todo move all repository instances to a class

    @SneakyThrows
    @GetMapping(SEE_SCORES_IN_TERM_PATH)//todo test later
    public JSONArray seeScoresInSpecifiedTerm(@RequestParam Long termId,
                                              @RequestHeader(value = "username") String username,
                                              @RequestHeader(value = "password") String password){
        return service.seeScoresInSpecifiedTerm(termId, username, password);
    }

    @SneakyThrows
    @GetMapping(SEE_SUMMERY_PATH)
    public String seeSummery(@RequestHeader(value = "username") String username,
                                @RequestHeader(value = "password") String password){
        return service.seeSummery(username, password);
    }
}
