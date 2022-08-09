package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.repositories.Repository;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.StudentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentSecurityService {
    private final ErrorChecker errorChecker;
    private final StudentService service;
    private final Repository repository;

    public CourseSectionRegistration signUpSection(Long courseSectionId, String token) {
        errorChecker.checkIsUser(token);
        String username = repository.findTokenByToken(token).getUsername();
        CourseSection courseSection = repository.findCourseSection(courseSectionId);
        Student student = repository.findStudentByUsername(username);
        errorChecker.checkCourseSectionRegistrationExists(courseSection, student);
        return service.signUpSection(student, courseSection);
    }

    public JSONArray seeScoresInSpecifiedTerm(Long termId, String token){
        errorChecker.checkIsUser(token);
        String username = repository.findTokenByToken(token).getUsername();
        return service.seeScoresInSpecifiedTerm(termId, username);
    }

    public String seeSummery(String token) {
        errorChecker.checkIsUser(token);
        String username = repository.findTokenByToken(token).getUsername();
        return service.seeSummery(username);
    }

    public String listCourseSectionStudents(Long courseSectionId, String token) {
        CourseSection courseSection = repository.findCourseSection(courseSectionId);
        errorChecker.checkIsInstructorOfCourseSectionOrAdmin(token, courseSection);
        List<CourseSectionRegistration> courseSectionRegistrations = repository
                .findCourseSectionRegistrationByCourseSection(courseSection);
        return service.listCourseSectionStudents(courseSectionRegistrations);
    }

}
