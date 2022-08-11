package tech.sobhan.golestan.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.dto.StudentAverageDTO;
import tech.sobhan.golestan.models.dto.SummeryDTO;
import tech.sobhan.golestan.services.StudentService;
import tech.sobhan.golestan.services.security.StudentSecurityService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
@RequiredArgsConstructor
public class StudentController {
    private final StudentService service;
    private final StudentSecurityService securityService;

    @PostMapping(SIGNUP_SECTION_PATH)
    public CourseSectionRegistration signUpSection(@RequestParam Long courseSectionId,
                                                   @RequestHeader String token) {
        securityService.signUpSection(courseSectionId, token);
        return service.signUpSection(courseSectionId, token);
    }

    @GetMapping(SEE_SCORES_IN_TERM_PATH)
    public EntityModel<StudentAverageDTO> seeScoresInSpecifiedTerm(@RequestParam Long termId, @RequestHeader String token) {
        securityService.seeScoresInSpecifiedTerm(token);
        return service.seeScoresInSpecifiedTerm(termId, token);
    }

    @GetMapping(SEE_SUMMERY_PATH)
    public SummeryDTO seeSummery(@RequestHeader String token) {
        securityService.seeSummery(token);
        return service.seeSummery(token);
    }
}
