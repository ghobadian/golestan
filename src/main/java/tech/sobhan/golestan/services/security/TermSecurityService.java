package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.TermService;

@Service
@RequiredArgsConstructor
public class TermSecurityService {
    private final TermService service;
    private final ErrorChecker errorChecker;

    public String list(String token) {
        errorChecker.checkIsUser(token);
        return service.list().toString();
    }

    public String create(String title, boolean open, String token) {
        errorChecker.checkIsAdmin(token);
        errorChecker.checkTermExists(title);
        errorChecker.checkTermExists(title);
        return service.create(title, open).toString();
    }

    public String read(Long id, String token) {
        errorChecker.checkIsUser(token);
        return service.read(id).toString();
    }

    public String update(String title, Boolean open, Long termId, String token) {
        errorChecker.checkIsAdmin(token);
        return service.update(title, open, termId).toString();
    }

    public void delete(Long id, String token) {
        errorChecker.checkIsAdmin(token);
        service.delete(id);
    }
}
