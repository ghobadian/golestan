package tech.sobhan.golestan.services;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.TermNotFoundException;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.repositories.TermRepository;
import tech.sobhan.golestan.repositories.UserRepository;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.*;

@Service
public class TermService {
    private final TermRepository termRepository;
    private final UserRepository userRepository;
    private final ErrorChecker errorChecker;


    public TermService(TermRepository termRepository, UserRepository userRepository, ErrorChecker errorChecker) {
        this.termRepository = termRepository;
        this.userRepository = userRepository;
        this.errorChecker = errorChecker;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return list().toString();
    }

    private List<Term> list() {
        return termRepository.findAll();
    }


    public String create(String title, boolean open, String username, String password) {

        errorChecker.checkIsAdmin(username, password);
        Term term = Term.builder().title(title).open(open).build();
        return create(term).toString();
    }

    public Term create(Term term) {
        if (termExists(list(), term)) return null;
        createLog(Term.class, term.getId());
        return termRepository.save(term);
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private Term read(Long id) {
        return termRepository.findById(id).orElseThrow(TermNotFoundException::new);
    }

    public String update(String title, boolean open, Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        update(title, open, id);
        return "OK";
    }

    private void update(String title, boolean open, Long id) {
        termRepository.findById(id).map(user -> {
            user.setTitle(title);
            user.setOpen(open);
            return termRepository.save(user);
        }).orElseGet(() -> {
            Term newTerm = Term.builder().id(id).title(title).open(open).build();
            return termRepository.save(newTerm);
        });
    }

    public String delete(Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        termRepository.delete(read(id));
        return "OK";
    }

    private static boolean termExists(List<Term> allTerms, Term term) {
        for (Term t : allTerms) {
            if(term.equals(t)){
                System.out.println("ERROR403 duplicate Terms");
                return true;
            }
        }
        return false;
    }
}
