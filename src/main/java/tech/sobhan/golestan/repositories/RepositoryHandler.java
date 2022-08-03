package tech.sobhan.golestan.repositories;

import lombok.Getter;
import org.springframework.stereotype.Component;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.business.exceptions.*;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Getter
public class RepositoryHandler {
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

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
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

    public void deleteInstructor(Instructor instructor) {
        instructorRepository.delete(instructor);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    public CourseSectionRegistration findCourseSectionRegistrationByCourseSectionAndStudent(Long courseSectionId, Long studentId) {
        return courseSectionRegistrationRepository
                .findByCourseSectionAndStudent(courseSectionId, studentId)
                .orElseThrow(CourseSectionRegistrationNotFoundException::new);
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Student findStudentByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return user.getStudent();
    }

    public List<CourseSectionRegistration> findCourseSectionRegistrationByStudent(Long studentId) {
        return courseSectionRegistrationRepository.findByStudent(studentId);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public List<Term> findAllTerms() {
        return termRepository.findAll();
    }

    public User findUserByStudent(Long id) {
        return userRepository.findByStudentId(id).orElseThrow(UserNotFoundException::new);
    }

    public User findUserByInstructor(Long id) {
        return userRepository.findByInstructorId(id).orElseThrow(UserNotFoundException::new);
    }

    public Term saveTerm(Term term) {
        return termRepository.save(term);
    }

    public void deleteTerm(Term term) {
        termRepository.delete(term);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public List<CourseSection> findAllCourseSections(){
        return courseSectionRepository.findAll();
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    public int findNumberOfStudentsInCourseSection(Long id) {
        return courseSectionRegistrationRepository.findNumberOfStudents(id);
    }

    public List<CourseSection> findCourseSectionByInstructorName(String instructorName) {
        List<CourseSection> allCourseSections = findAllCourseSections();
        List<CourseSection> output = new ArrayList<>();
        for (CourseSection courseSection : allCourseSections) {
            Long currentCourseSectionInstructorId = courseSection.getInstructor().getId();
            String currentCourseSectionInstructorName = findUserByInstructor(currentCourseSectionInstructorId).getName();
            if(instructorName.equals(currentCourseSectionInstructorName)){
                output.add(courseSection);
            }
        }
        return output;
    }

    public List<CourseSection> findCourseSectionByCourseName(String courseTitle) {
        return courseSectionRepository.findByCourseTitle(courseTitle);
    }

    public List<CourseSection> findCourseSectionByInstructorNameAndCourseName(String instructorName, String courseName) {
        Set<CourseSection> courseSectionsByInstructorName = new HashSet<>(findCourseSectionByCourseName(courseName));
        Set<CourseSection> courseSectionsByCourseName = new HashSet<>(findCourseSectionByInstructorName(instructorName));
        courseSectionsByCourseName.retainAll(courseSectionsByInstructorName);
        return new ArrayList<>(courseSectionsByCourseName);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteAll() {
        courseSectionRegistrationRepository.deleteAll();
        courseSectionRepository.deleteAll();
        courseRepository.deleteAll();
        termRepository.deleteAll();
        userRepository.deleteAll();
        studentRepository.deleteAll();
        instructorRepository.deleteAll();
    }
}
