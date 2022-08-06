package tech.sobhan.golestan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.services.security.TermSecurityService;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class TermController {
    private final TermSecurityService service;

    public TermController(TermSecurityService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(TERM_LIST_PATH)
    private String list(@RequestHeader(value = "username") String username,
                            @RequestHeader(value = "password") String password){
        return service.list(username, password);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(TERM_CREATE_PATH)
    private String create(@RequestParam String title, boolean open,
                        @RequestHeader(value = "username") String username,
                        @RequestHeader(value = "password") String password){
        return service.create(title, open, username, password);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(TERM_READ_PATH)
    private String read(@PathVariable Long id,
                      @RequestHeader(value = "username") String username,
                      @RequestHeader(value = "password") String password){
        return service.read(id, username, password);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(TERM_UPDATE_PATH)
    private String update(@RequestParam(required = false) String title,
                          @RequestParam(required = false) Boolean open,
                          @PathVariable Long id,
                          @RequestHeader(value = "username") String username,
                          @RequestHeader(value = "password") String password){
        return service.update(title, open, id, username, password);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(TERM_DELETE_PATH)
    private void delete(@PathVariable Long id,
                        @RequestHeader(value = "username") String username,
                        @RequestHeader(value = "password") String password){
        service.delete(id, username, password);
    }
}
