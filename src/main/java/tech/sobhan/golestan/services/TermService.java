package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.repositories.Repository;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;

@Service
@RequiredArgsConstructor
public class TermService {
    private final Repository repository;

    public List<Term> list() {
        return repository.findAllTerms();
    }

    public Term create(String title, boolean open) {
        Term term = Term.builder().title(title).open(open).build();
        createLog(Term.class, term.getId());
        return repository.saveTerm(term);
    }

    public Term read(Long termId) {
        return repository.findTerm(termId);
    }

    public Term update(String title, Boolean open, Long termId) {
        Term term = repository.findTerm(termId);
        changeTitle(title, term);
        changeOpen(open, term);
        return repository.saveTerm(term);
    }

    private void changeOpen(Boolean open, Term term) {
        if(open != null){
            term.setOpen(open);
        }
    }

    private void changeTitle(String title, Term term) {
        if(title !=null){
            term.setTitle(title);
        }
    }

    public void delete(Long id) {
        Term term = repository.findTerm(id);
        repository.deleteTerm(term);
    }
}
