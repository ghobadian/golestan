package tech.sobhan.golestan.services.security;

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
public class StudentSecurityService {
    private final ErrorChecker errorChecker;
    private final StudentService service;
    private final Repository repository;

    public StudentSecurityService(ErrorChecker errorChecker, StudentService service, Repository repository) {
        this.errorChecker = errorChecker;
        this.service = service;
        this.repository = repository;
    }

    public String signUpSection(Long courseSectionId, String username, String password) {
        errorChecker.checkIsUser(username, password);
        CourseSection courseSection = repository.findCourseSection(courseSectionId);
        Student student = repository.findStudentByUsername(username);
        errorChecker.checkCourseSectionRegistrationExists(courseSection, student);
        return service.signUpSection(student, courseSection);
    }

    public JSONArray seeScoresInSpecifiedTerm(Long termId, String username, String password){
        errorChecker.checkIsUser(username, password);
        return service.seeScoresInSpecifiedTerm(termId, username);
    }

    public String seeSummery(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return service.seeSummery(username);
    }

    public String listCourseSectionStudents(Long courseSectionId, String username, String password) {
        CourseSection courseSection = repository.findCourseSection(courseSectionId);
        errorChecker.checkIsInstructorOfCourseSectionOrAdmin(username, password, courseSection);
        List<CourseSectionRegistration> courseSectionRegistrations = repository
                .findCourseSectionRegistrationByCourseSection(courseSection);
        return service.listCourseSectionStudents(courseSectionRegistrations);
    }

}
