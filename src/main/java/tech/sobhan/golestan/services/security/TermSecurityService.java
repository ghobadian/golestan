package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.TermService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermSecurityService {
    private final TermService service;
    private final ErrorChecker errorChecker;

    public List<Term> list(String token) {
        errorChecker.checkIsUser(token);
        return service.list();
    }

    public Term create(String title, boolean open, String token) {
        errorChecker.checkIsAdmin(token);
        errorChecker.checkTermExists(title);
        errorChecker.checkTermExists(title);
        return service.create(title, open);
    }

    public Term read(Long id, String token) {
        errorChecker.checkIsUser(token);
        return service.read(id);
    }

    public Term update(String title, Boolean open, Long termId, String token) {
        errorChecker.checkIsAdmin(token);
        return service.update(title, open, termId);
    }

    public void delete(Long id, String token) {
        errorChecker.checkIsAdmin(token);
        service.delete(id);
    }
}
