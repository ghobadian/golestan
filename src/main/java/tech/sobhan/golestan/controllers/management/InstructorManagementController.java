package tech.sobhan.golestan.controllers.management;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.services.InstructorService;

import static tech.sobhan.golestan.constants.ApiPaths.INSTRUCTOR_DELETE_PATH;
import static tech.sobhan.golestan.constants.ApiPaths.INSTRUCTOR_UPDATE_PATH;

public class InstructorManagementController {
    private final InstructorService service;

    public InstructorManagementController(InstructorService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(INSTRUCTOR_DELETE_PATH)
    private void delete(@PathVariable Long instructorId,
                        @RequestHeader(value = "username") String username,
                        @RequestHeader(value = "password") String password){
        service.delete(instructorId, username, password);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(INSTRUCTOR_UPDATE_PATH)
    private void update(@RequestParam Rank rank, @PathVariable Long instructorId,
                        @RequestHeader(value = "username") String username,
                        @RequestHeader(value = "password") String password){
        service.update(rank, instructorId, username, password);
    }
}
