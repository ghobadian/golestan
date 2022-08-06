package tech.sobhan.golestan.services.security;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.TermService;

@Service
public class TermSecurityService {
    private final TermService service;
    private final ErrorChecker errorChecker;

    public TermSecurityService(TermService service, ErrorChecker errorChecker) {
        this.service = service;
        this.errorChecker = errorChecker;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return service.list().toString();
    }

    public String create(String title, boolean open, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        errorChecker.checkTermExists(title);
        errorChecker.checkTermExists(title);
        return service.create(title, open).toString();
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return service.read(id).toString();
    }

    public String update(String title, Boolean open, Long termId, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        return service.update(title, open, termId).toString();
    }

    public void delete(Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        service.delete(id);
    }
}
