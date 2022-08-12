package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.security.ErrorChecker;

@Service
@RequiredArgsConstructor
public class CourseSectionSecurityService {
    private final ErrorChecker errorChecker;
    public void list(String token) {
        errorChecker.checkIsUser(token);
    }

    public void listStudentsByCourseSection(Long courseSectionId, String token) {
        errorChecker.checkIsInstructorOfCourseSectionOrAdmin(token, courseSectionId);
    }

    public void create(Long courseId, Long instructorId, Long termId, String token) {
        errorChecker.checkIsInstructorOfCourseSection(token, courseId, instructorId, termId);
        errorChecker.checkCourseSectionExists(courseId, instructorId, termId);
    }

    public void read(String token) {
        errorChecker.checkIsUser(token);
    }

    public void update(String token, Long courseSectionId) {
        errorChecker.checkIsInstructorOfCourseSection(token, courseSectionId);
    }

    public void delete(Long courseSectionId, String token) {
        errorChecker.checkIsInstructorOfCourseSection(token, courseSectionId);
        errorChecker.checkCourseSectionIsNotEmpty(courseSectionId);
    }
}
