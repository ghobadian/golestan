package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.security.ErrorChecker;

@Service
@RequiredArgsConstructor
public class StudentSecurityService {
    private final ErrorChecker errorChecker;

    public void signUpSection(Long courseSectionId, String token) {
        errorChecker.checkIsUser(token);
        errorChecker.checkCourseSectionRegistrationExists(courseSectionId, token);
    }

    public void seeScoresInSpecifiedTerm(String token) {
        errorChecker.checkIsUser(token);
    }

    public void seeSummery(String token) {
        errorChecker.checkIsUser(token);
    }
}
