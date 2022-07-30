package tech.sobhan.golestan.services;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.TermNotFoundException;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.repositories.TermRepository;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;

@Service
public class TermService {
    private final TermRepository termRepository;

    public TermService(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public List<Term> list() {
        List<Term> allTerms = termRepository.findAll();
//        if(allTerms.isEmpty()){
//            throw new TermNotFoundException();
//        }
        return allTerms;
    }

    public Term create(String title, boolean open) {
        Term term = Term.builder().title(title).open(open).build();
        return create(term);
    }

    public Term create(Term term) {
        if (termExists(list(), term)) return null;
        createLog(Term.class, term.getId());
        return termRepository.save(term);
    }

    public Term read(Long id) {
        return termRepository.findById(id).orElseThrow(TermNotFoundException::new);
    }

    public void update(String title, boolean open, Long id) {
        termRepository.findById(id).map(user -> {
            user.setTitle(title);
            user.setOpen(open);
            return termRepository.save(user);//todo sus for saveUser instead of user
        }).orElseGet(() -> {
            Term newTerm = Term.builder().id(id).title(title).open(open).build();
            return termRepository.save(newTerm);
        });
    }

    public void delete(Long id) {
        termRepository.delete(read(id));
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
