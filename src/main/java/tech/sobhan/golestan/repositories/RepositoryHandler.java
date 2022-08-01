package tech.sobhan.golestan.repositories;

import lombok.Getter;
import org.springframework.stereotype.Component;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.business.exceptions.*;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;

import java.util.List;

@Component
@Getter
public final class RepositoryHandler {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final TermRepository termRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    public RepositoryHandler(UserRepository userRepository,
                             StudentRepository studentRepository, CourseRepository courseRepository,
                             InstructorRepository instructorRepository,
                             TermRepository termRepository,
                             CourseSectionRepository courseSectionRepository,
                             CourseSectionRegistrationRepository courseSectionRegistrationRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.termRepository = termRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public List<CourseSection> findCourseSectionByTerm(Long termId){
        List<CourseSection> courseSections = courseSectionRepository.findByTerm(termId);
        if(courseSections.isEmpty()) throw new CourseSectionNotFoundException();
        return courseSections;
    }

    public Instructor findInstructor(Long id) {
        if(id == null) throw new InstructorNotFoundException();
        return instructorRepository.findById(id).orElseThrow(InstructorNotFoundException::new);
    }

    public Course findCourse(Long id) {
        if(id == null) throw new CourseNotFoundException();
        return courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
    }

    public CourseSectionRegistration saveCourseSectionRegistration(CourseSectionRegistration courseSectionRegistration) {
        return courseSectionRegistrationRepository.save(courseSectionRegistration);
    }

    public Term findTerm(Long id) {
        if(id == null) throw new TermNotFoundException();
        return termRepository.findById(id).orElseThrow(TermNotFoundException::new);
    }

    public CourseSection saveCourseSection(CourseSection courseSection) {
        return courseSectionRepository.save(courseSection);
    }

    public CourseSection findCourseSection(Long id) {
        if(id == null) throw new CourseSectionNotFoundException();
        return courseSectionRepository.findById(id).orElseThrow(CourseSectionNotFoundException::new);
    }

    public List<CourseSectionRegistration> findCourseSectionRegistrationByCourseSection(Long courseSectionId) {
        List<CourseSectionRegistration> courseSectionRegistrations = courseSectionRegistrationRepository
                .findByCourseSection(courseSectionId);
        if(courseSectionRegistrations.isEmpty()) throw new CourseSectionRegistrationNotFoundException();
        return courseSectionRegistrations;

    }

    public void deleteCourseSection(CourseSection specifiedCourseSection) {
        courseSectionRepository.delete(specifiedCourseSection);
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public Instructor saveInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    public User getUserByUsername(String username) {
        if(username == null) throw new UserNotFoundException();
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public List<CourseSectionRegistration> findAllCourseSectionRegistrations() {
        List<CourseSectionRegistration> courseSectionRegistrations = courseSectionRegistrationRepository.findAll();
        if(courseSectionRegistrations.isEmpty()) throw new CourseSectionNotFoundException();
        return courseSectionRegistrations;
    }

    public List<Instructor> findAllInstructors() {
        return instructorRepository.findAll();
    }

    public List<CourseSection> findCourseSectionByInstructor(Long id) {
        return courseSectionRepository.findByInstructor(id);
    }
}
