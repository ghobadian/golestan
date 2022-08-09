package tech.sobhan.golestan.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.services.security.InstructorSecurityService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorSecurityService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(INSTRUCTOR_LIST_PATH)
    private String list(@RequestHeader String token){//todo send also the user
        return service.list(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(INSTRUCTOR_READ_PATH)
    private String read(@PathVariable Long id, @RequestHeader String token){
        return service.read(id, token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(INSTRUCTOR_DELETE_PATH)
    private void delete(@PathVariable Long instructorId, @RequestHeader String token){
        service.delete(instructorId, token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(INSTRUCTOR_UPDATE_PATH)
    private Instructor update(@RequestParam Rank rank, @PathVariable Long instructorId, @RequestHeader String token){
        return service.update(rank, instructorId, token);
    }

    @PostMapping(GIVE_SINGLE_MARK_PATH)
    public CourseSectionRegistration giveMark(@RequestParam Long courseSectionId,
                                              @PathVariable Long studentId,
                                              @RequestParam Double score,
                                              @RequestHeader String token){
        return service.giveMark(token, courseSectionId, studentId, score);
    }

    @PostMapping(GIVE_MULTIPLE_MARK_PATH)
    public String giveMultipleMarks(@RequestParam Long courseSectionId,
                                    @RequestParam JSONArray studentIds,
                                    @RequestParam JSONArray scores,
                                    @RequestHeader String token){
        return service.giveMultipleMarks(token, courseSectionId, studentIds, scores);
    }
}
