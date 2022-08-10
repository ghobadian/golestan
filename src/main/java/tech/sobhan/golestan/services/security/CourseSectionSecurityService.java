package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.dto.CourseSectionDTO;
import tech.sobhan.golestan.models.dto.StudentDTO;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.CourseSectionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSectionSecurityService {
    private final ErrorChecker errorChecker;
    private final CourseSectionService service;
    private final Repo repo;

    public List<CourseSection> list(Long termId, String token, String instructorName,
                                    String courseName, Integer pageNumber, Integer maxInEachPage) {
        errorChecker.checkIsUser(token);
        return service.list(termId, instructorName, courseName, pageNumber, maxInEachPage);
    }

    public List<StudentDTO> listCourseSectionStudents(Long courseSectionId, String token) {
        CourseSection courseSection = repo.findCourseSection(courseSectionId);
        errorChecker.checkIsInstructorOfCourseSectionOrAdmin(token, courseSection);
        List<CourseSectionRegistration> courseSectionRegistrations = repo
                .findCourseSectionRegistrationByCourseSection(courseSection);
        return service.listCourseSectionStudents(courseSectionRegistrations);
    }

    public CourseSection create(Long courseId, Long instructorId, Long termId, String token) {
        CourseSection courseSection = buildCourseSection(courseId, instructorId, termId);
        Term term = repo.findTerm(termId);
        errorChecker.checkIsInstructorOfCourseSection(token, courseSection);
        errorChecker.checkCourseSectionExists(term, courseSection);
        return service.create(courseSection);
    }

    private CourseSection buildCourseSection(Long courseId, Long instructorId, Long termId) {
        Course course = repo.findCourse(courseId);
        Instructor instructor = repo.findInstructor(instructorId);
        Term term = repo.findTerm(termId);
        return CourseSection.builder().instructor(instructor).course(course).term(term).build();
    }

    public CourseSectionDTO read(Long id, String token) {
        errorChecker.checkIsUser(token);
        return service.read(id);
    }

    public CourseSection update(Long termId, Long courseId, Long instructorId, Long courseSectionId, String token) {//todo add DTO
        errorChecker.checkIsInstructorOfCourseSection(token, courseSectionId);
        return service.update(termId, courseId, instructorId, courseSectionId);
    }

    public void delete(Long courseSectionId, String token) {
        CourseSection courseSection = repo.findCourseSection(courseSectionId);
        errorChecker.checkIsInstructorOfCourseSection(token, courseSectionId);
        errorChecker.checkCourseSectionIsNotEmpty(courseSection);
        service.delete(courseSection);
    }
}
