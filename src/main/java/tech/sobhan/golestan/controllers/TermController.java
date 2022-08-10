package tech.sobhan.golestan.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.services.security.TermSecurityService;

import java.util.List;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
@RequiredArgsConstructor
public class TermController {
    private final TermSecurityService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(TERM_LIST_PATH)
    private List<Term> list(@RequestHeader String token) {
        return service.list(token);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(TERM_CREATE_PATH)
    private Term create(@RequestParam String title, boolean open, @RequestHeader String token) {
        return service.create(title, open, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(TERM_READ_PATH)
    private Term read(@PathVariable Long id, @RequestHeader String token) {
        return service.read(id, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(TERM_UPDATE_PATH)
    private Term update(@RequestParam(required = false) String title,
                          @RequestParam(required = false) Boolean open,
                          @PathVariable Long id,
                          @RequestHeader String token) {
        return service.update(title, open, id, token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(TERM_DELETE_PATH)
    private void delete(@PathVariable Long id, @RequestHeader String token) {
        service.delete(id, token);
    }
}
