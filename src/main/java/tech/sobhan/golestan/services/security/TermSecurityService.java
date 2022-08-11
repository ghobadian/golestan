package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.security.ErrorChecker;

@Service
@RequiredArgsConstructor
public class TermSecurityService {
    private final ErrorChecker errorChecker;

    public void list(String token) {
        errorChecker.checkIsUser(token);
    }

    public void create(String title, String token) {
        errorChecker.checkIsAdmin(token);
        errorChecker.checkTermExists(title);
        errorChecker.checkTermExists(title);
    }

    public void read(String token) {
        errorChecker.checkIsUser(token);
    }

    public void update(String token) {
        errorChecker.checkIsAdmin(token);
    }

    public void delete(String token) {
        errorChecker.checkIsAdmin(token);
    }
}
