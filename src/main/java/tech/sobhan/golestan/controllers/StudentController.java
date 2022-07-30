package tech.sobhan.golestan.controllers;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.services.StudentService;


import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping(SIGNUP_SECTION_PATH)
    public void signUpSection(@RequestParam Long courseSectionId, @RequestHeader(value = "username") String username,
                              @RequestHeader(value = "password") String password){//todo اینجا چطور به برنامه بفهمونم که کاربر فعلی کیه؟
        service.signUpSection(courseSectionId, username, password);//todo چطور بتونم از اون کوکی که توی مرورگر نشون میده استفاده کنم
    }//todo move all repository instances to a class

    @SneakyThrows
    @PostMapping(SEE_SCORES_IN_TERM_PATH)//todo test later
    public String seeScoresInSpecifiedTerm(@PathVariable Long term_id, @RequestHeader(value = "username") String username,
                                           @RequestHeader(value = "password") String password){//todo
        return service.seeScoresInSpecifiedTerm(term_id, username, password);
    }

    @SneakyThrows
    @GetMapping(SEE_SUMMERY_PATH)
    public String seeSummery(@RequestHeader(value = "username") String username,
                                @RequestHeader(value = "password") String password){
        return service.seeSummery(username, password);
    }
}
