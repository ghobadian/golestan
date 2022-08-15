package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.dto.CourseSectionDTOLight;
import tech.sobhan.golestan.models.dto.StudentDTO;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseSectionService {
    private final Repo repo;

    public List<CourseSection> list(Long termId, String instructorName,
                       String courseName, int page, int number) {
        Term term = repo.findTerm(termId);
        return repo.findCourseSectionsByTermAndInstructorNameAndCourseTitle(term, instructorName, courseName,
                PageRequest.of(page, number));
    }

    public List<StudentDTO> listStudentsByCourseSection(Long courseSectionId) {
        CourseSection cs = repo.findCourseSection(courseSectionId);
        List<CourseSectionRegistration> courseSectionRegistrations = repo
                .findCourseSectionRegistrationByCourseSection(cs);
        return courseSectionRegistrations.stream()
                .map(csr -> getStudentDetails(csr, repo))
                .collect(Collectors.toList());
    }

    private StudentDTO getStudentDetails(CourseSectionRegistration csr, Repo repo) {
        Student student = csr.getStudent();
        User user = repo.findUserByStudent(student.getId());
        return StudentDTO.builder().id(student.getId()).name(user.getName())
                .number(user.getPhone()).score(csr.getScore()).build();
    }

    public CourseSection create(Long courseId, Long instructorId, Long termId) {
        CourseSection courseSection = buildCourseSection(courseId, instructorId, termId);
        log.info("CourseSection " + courseSection + "created");
        return repo.saveCourseSection(courseSection);
    }

    private CourseSection buildCourseSection(Long courseId, Long instructorId, Long termId) {
        Course course = repo.findCourse(courseId);
        Instructor instructor = repo.findInstructor(instructorId);
        Term term = repo.findTerm(termId);
        return CourseSection.builder().instructor(instructor).course(course).term(term).build();
    }

    public CourseSectionDTOLight read(Long id) {
        CourseSection courseSection = repo.findCourseSection(id);
        int numberOfStudents = repo.findNumberOfStudentsInCourseSection(courseSection);
        return CourseSectionDTOLight.builder().courseSection(courseSection).numberOfStudents(numberOfStudents).build();
    }

    public CourseSection update(Long termId, Long courseId, Long instructorId, Long courseSectionId) {
        CourseSection courseSection = repo.findCourseSection(courseSectionId);
        updateTerm(termId, courseSection);
        updateCourse(courseId, courseSection);
        updateInstructor(instructorId, courseSection);
        return repo.saveCourseSection(courseSection);
    }

    private void updateInstructor(Long instructorId, CourseSection courseSection) {
        if (repo.instructorExistsById(instructorId)) {
            Instructor instructor = repo.findInstructor(instructorId);
            courseSection.setInstructor(instructor);
        }
    }

    private void updateCourse(Long courseId, CourseSection courseSection) {
        if (repo.courseExistsById(courseId)) {
            Course course = repo.findCourse(courseId);
            courseSection.setCourse(course);
        }
    }

    private void updateTerm(Long termId, CourseSection courseSection) {
        if (repo.termExistsById(termId)) {
            Term term = repo.findTerm(termId);
            courseSection.setTerm(term);
        }
    }

    public void delete(Long courseSectionId) {
        CourseSection cs = repo.findCourseSection(courseSectionId);
        log.info("CourseSection " + cs + " deleted");
        repo.deleteCourseSection(cs);
    }
}
