package tech.sobhan.golestan.services;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.duplication.TermDuplicationException;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.repositories.RepositoryHandler;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;

@Service
public class TermService {
    private final RepositoryHandler repositoryHandler;
    private final ErrorChecker errorChecker;

    public TermService(RepositoryHandler repositoryHandler, ErrorChecker errorChecker) {
        this.repositoryHandler = repositoryHandler;
        this.errorChecker = errorChecker;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return list().toString();
    }

    private List<Term> list() {
        return repositoryHandler.findAllTerms();
    }


    public String create(String title, boolean open, String username, String password) {

        errorChecker.checkIsAdmin(username, password);
        Term term = Term.builder().title(title).open(open).build();
        return create(term).toString();
    }

    public Term create(Term term) {
        if (termExists(list(), term)) throw new TermDuplicationException();//todo implement in all service classes
        createLog(Term.class, term.getId());
        return repositoryHandler.saveTerm(term);
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private Term read(Long termId) {
        return repositoryHandler.findTerm(termId);
    }

    public String update(String title, Boolean open, Long termId, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        update(title, open, termId);
        return "OK";
    }

    private void update(String title, Boolean open, Long termId) {
        Term term = repositoryHandler.findTerm(termId);
        if(title!=null){
            term.setTitle(title);
        }
        if(open != null){
            term.setOpen(open);
        }
        repositoryHandler.saveTerm(term);
    }

    public String delete(Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        Term term = repositoryHandler.findTerm(id);
        repositoryHandler.deleteTerm(term);
        return "OK";
    }

    private static boolean termExists(List<Term> allTerms, Term term) {
        for (Term t : allTerms) {
            if(term.equals(t)){
                return true;
            }
        }
        return false;
    }
}
