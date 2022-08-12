package tech.sobhan.golestan.controllers;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.services.InstructorService;
import tech.sobhan.golestan.services.security.InstructorSecurityService;

import java.util.List;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService service;
    private final InstructorSecurityService securityService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(INSTRUCTOR_LIST_PATH)
    private List<Instructor> list(@RequestHeader String token, @RequestParam int page, @RequestParam int number) {
        securityService.list(token);
        return service.list(page, number);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(INSTRUCTOR_READ_PATH)
    private Instructor read(@PathVariable Long id, @RequestHeader String token) {
        securityService.read(token);
        return service.read(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(INSTRUCTOR_DELETE_PATH)
    private void delete(@PathVariable Long instructorId, @RequestHeader String token) {
        securityService.delete(token);
        service.delete(instructorId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(INSTRUCTOR_UPDATE_PATH)
    private Instructor update(@RequestParam Rank rank, @PathVariable Long instructorId, @RequestHeader String token) {
        securityService.update(token);
        return service.update(rank, instructorId);
    }

    @PostMapping(GIVE_SINGLE_MARK_PATH)
    public CourseSectionRegistration giveMark(@RequestParam Long courseSectionId,
                                              @PathVariable Long studentId,
                                              @RequestParam Double score,
                                              @RequestHeader String token) {
        securityService.giveMark(token, courseSectionId);
        return service.giveMark(courseSectionId, studentId, score);
    }

    @PostMapping(GIVE_MULTIPLE_MARK_PATH)
    public List<CourseSectionRegistration> giveMultipleMarks(@RequestParam Long courseSectionId,
                                    @RequestParam JSONArray studentIds,
                                    @RequestParam JSONArray scores,
                                    @RequestHeader String token) {
        securityService.giveMultipleMarks(token, courseSectionId);
        return service.giveMultipleMarks(courseSectionId, studentIds, scores);
    }
}
