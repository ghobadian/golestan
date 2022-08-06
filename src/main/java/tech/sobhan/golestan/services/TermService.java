package tech.sobhan.golestan.services;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.repositories.Repository;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;

@Service
public class TermService {
    private final Repository repository;
    private final ErrorChecker errorChecker;

    public TermService(Repository repository, ErrorChecker errorChecker) {
        this.repository = repository;
        this.errorChecker = errorChecker;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return list().toString();
    }

    private List<Term> list() {
        return repository.findAllTerms();
    }


    public String create(String title, boolean open, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        errorChecker.checkTermExists(title);
        Term term = Term.builder().title(title).open(open).build();
        return create(term).toString();
    }

    public Term create(Term term) {
        errorChecker.checkTermExists(term.getTitle());
        createLog(Term.class, term.getId());
        return repository.saveTerm(term);
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private Term read(Long termId) {
        return repository.findTerm(termId);
    }

    public String update(String title, Boolean open, Long termId, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        update(title, open, termId);
        return "OK";
    }

    private void update(String title, Boolean open, Long termId) {
        Term term = repository.findTerm(termId);
        if(title!=null){
            term.setTitle(title);
        }
        if(open != null){
            term.setOpen(open);
        }
        repository.saveTerm(term);
    }

    public void delete(Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        Term term = repository.findTerm(id);
        repository.deleteTerm(term);
    }
}
