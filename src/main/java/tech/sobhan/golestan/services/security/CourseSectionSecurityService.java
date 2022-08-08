package tech.sobhan.golestan.services.security;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.repositories.Repository;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.CourseSectionService;

@Service
public class CourseSectionSecurityService {
    private final ErrorChecker errorChecker;
    private final CourseSectionService service;

    private final Repository repository;

    public CourseSectionSecurityService(ErrorChecker errorChecker, CourseSectionService service, Repository repository) {
        this.errorChecker = errorChecker;
        this.service = service;
        this.repository = repository;
    }

    public String list(Long termId, String token, String instructorName,
                       String courseName, Integer pageNumber, Integer maxInEachPage) {
        errorChecker.checkIsUser(token);
        return service.list(termId, instructorName, courseName, pageNumber, maxInEachPage);
    }

    public String create(Long courseId, Long instructorId, Long termId, String token) {
        CourseSection courseSection = buildCourseSection(courseId, instructorId, termId);
        Term term = repository.findTerm(termId);
        errorChecker.checkIsInstructorOfCourseSection(token, courseSection);
        errorChecker.checkCourseSectionExists(term, courseSection);
        return service.create(courseSection).toString();
    }

    private CourseSection buildCourseSection(Long courseId, Long instructorId, Long termId) {
        Course course = repository.findCourse(courseId);
        Instructor instructor = repository.findInstructor(instructorId);
        Term term = repository.findTerm(termId);
        return CourseSection.builder().instructor(instructor).course(course).term(term).build();
    }

    public String read(Long id, String token) {
        errorChecker.checkIsUser(token);
        return service.read(id).toString();
    }

    public String update(Long termId, Long courseId, Long instructorId, Long courseSectionId, String token) {//todo add DTO
        errorChecker.checkIsInstructorOfCourseSection(token, courseSectionId);
        return service.update(termId, courseId, instructorId, courseSectionId);
    }

    public void delete(Long courseSectionId, String token) {
        CourseSection courseSection = repository.findCourseSection(courseSectionId);
        errorChecker.checkIsInstructorOfCourseSection(token, courseSectionId);
        errorChecker.checkCourseSectionIsNotEmpty(courseSection);
        service.delete(courseSection);
    }
}
