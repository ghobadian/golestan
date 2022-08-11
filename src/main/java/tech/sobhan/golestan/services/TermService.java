package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.Term;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermService {
    private final Repo repo;

    public List<Term> list() {
        return repo.findAllTerms();
    }

    public Term create(String title, boolean open) {
        Term term = Term.builder().title(title).open(open).build();
        log.info("Term with title: " + title + " created");
        return repo.saveTerm(term);
    }

    public Term read(Long termId) {
        return repo.findTerm(termId);
    }

    public Term update(String title, Boolean open, Long termId) {
        Term term = repo.findTerm(termId);
        changeTitle(title, term);
        changeOpen(open, term);
        return repo.saveTerm(term);
    }

    private void changeOpen(Boolean open, Term term) {
        if(open != null) {
            term.setOpen(open);
        }
    }

    private void changeTitle(String title, Term term) {
        if(title !=null) {
            term.setTitle(title);
        }
    }

    public void delete(Long id) {
        Term term = repo.findTerm(id);
        repo.deleteTerm(term);
        log.info("Term with title: " + term.getTitle() + " deleted");
    }
}
