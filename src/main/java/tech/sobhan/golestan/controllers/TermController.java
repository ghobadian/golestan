package tech.sobhan.golestan.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.services.TermService;
import tech.sobhan.golestan.services.security.TermSecurityService;

import java.util.List;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
@RequiredArgsConstructor
public class TermController {
    private final TermService service;
    private final TermSecurityService securityService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(TERM_LIST_PATH)
    private List<Term> list(@RequestHeader String token, @RequestParam int page, @RequestParam int number) {
        securityService.list(token);
        return service.list(page, number);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(TERM_CREATE_PATH)
    private Term create(@RequestParam String title, boolean open, @RequestHeader String token) {
        securityService.create(title, token);
        return service.create(title, open);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(TERM_READ_PATH)
    private Term read(@PathVariable Long id, @RequestHeader String token) {
        securityService.read(token);
        return service.read(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(TERM_UPDATE_PATH)
    private Term update(@RequestParam(required = false) String title,
                          @RequestParam(required = false) Boolean open,
                          @PathVariable Long id,
                          @RequestHeader String token) {
        securityService.update(token);
        return service.update(title, open, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(TERM_DELETE_PATH)
    private void delete(@PathVariable Long id, @RequestHeader String token) {
        securityService.delete(token);
        service.delete(id);
    }
}
