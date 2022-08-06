package tech.sobhan.golestan.controllers;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.services.InstructorService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class InstructorController {
    private final InstructorService service;

    public InstructorController(InstructorService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(INSTRUCTOR_LIST_PATH)
    private String list(@RequestHeader(value = "username") String username,
                                  @RequestHeader(value = "password") String password){//todo send also the user
        return service.list(username, password);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(INSTRUCTOR_READ_PATH)
    private String read(@PathVariable Long id,
                            @RequestHeader(value = "username") String username,
                            @RequestHeader(value = "password") String password){
        return service.read(id, username, password);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(INSTRUCTOR_DELETE_PATH)
    private void delete(@PathVariable Long instructorId,
                        @RequestHeader(value = "username") String username,
                        @RequestHeader(value = "password") String password){
        service.delete(instructorId, username, password);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(INSTRUCTOR_UPDATE_PATH)
    private void update(@RequestParam Rank rank, @PathVariable Long instructorId,
                        @RequestHeader(value = "username") String username,
                        @RequestHeader(value = "password") String password){
        service.update(rank, instructorId, username, password);
    }

    @PostMapping(GIVE_SINGLE_MARK_PATH)
    public String giveMark(@RequestParam Long courseSectionId,
                           @PathVariable Long studentId,
                           @RequestParam Double score,
                           @RequestHeader(value = "username") String username,
                           @RequestHeader(value = "password") String password){
        return service.giveMark(username, password, courseSectionId, studentId, score);
    }

    @PostMapping(GIVE_MULTIPLE_MARK_PATH)
    public String giveMultipleMarks(@RequestParam Long courseSectionId,
                                    @RequestParam JSONArray studentIds,
                                    @RequestParam JSONArray scores,
                                    @RequestHeader(value = "username") String username,
                                    @RequestHeader(value = "password") String password){
        return service.giveMultipleMarks(username, password, courseSectionId, studentIds, scores);
    }
}
