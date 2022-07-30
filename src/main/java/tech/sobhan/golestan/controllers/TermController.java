package tech.sobhan.golestan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.services.TermService;

import java.util.List;

import static tech.sobhan.golestan.constants.ApiPaths.*;

@RestController
public class TermController {
    private final TermService service;

    public TermController(TermService service) {
        this.service = service;
    }

    @GetMapping(TERM_LIST_PATH)
    private List<Term> list(){
        return service.list();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(TERM_CREATE_PATH)
    private Term create(@RequestParam String title, boolean open){
        return service.create(title, open);
    }

    @GetMapping(TERM_READ_PATH)
    private Term read(@PathVariable Long id){
        return service.read(id);
    }//todo add advice

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(TERM_UPDATE_PATH)
    private void update(@RequestParam String title, @RequestParam boolean open, @PathVariable Long id){
        service.update(title, open, id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(TERM_DELETE_PATH)
    private void delete(@PathVariable Long id){
        service.delete(id);
    }
}
