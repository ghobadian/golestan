package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.CourseService;

@Service
@RequiredArgsConstructor
public class CourseSecurityService {
    private final ErrorChecker errorChecker;

    public void list(String token) {
        errorChecker.checkIsUser(token);
    }

    public void create(String title, String token) {
        errorChecker.checkIsAdmin(token);
        errorChecker.checkCourseExistsByTitle(title);
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
