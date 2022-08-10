package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.dto.StudentAverageDTO;
import tech.sobhan.golestan.models.dto.SummeryDTO;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.StudentService;

@Service
@RequiredArgsConstructor
public class StudentSecurityService {
    private final ErrorChecker errorChecker;
    private final StudentService service;
    private final Repo repo;

    public CourseSectionRegistration signUpSection(Long courseSectionId, String token) {
        errorChecker.checkIsUser(token);
        String username = repo.findTokenByToken(token).getUsername();
        CourseSection courseSection = repo.findCourseSection(courseSectionId);
        Student student = repo.findStudentByUsername(username);
        errorChecker.checkCourseSectionRegistrationExists(courseSection, student);
        return service.signUpSection(student, courseSection);
    }

    public StudentAverageDTO seeScoresInSpecifiedTerm(Long termId, String token) {
        errorChecker.checkIsUser(token);
        String username = repo.findTokenByToken(token).getUsername();
        return service.seeScoresInSpecifiedTerm(termId, username);
    }

    public SummeryDTO seeSummery(String token) {
        errorChecker.checkIsUser(token);
        String username = repo.findTokenByToken(token).getUsername();
        return service.seeSummery(username);
    }
}
