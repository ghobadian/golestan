package tech.sobhan.golestan.dao;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.business.exceptions.notFound.*;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class Repo {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final TermRepository termRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;
    private final Map<String,String> tokenRepository;

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User getUserByUsername(String username) {
        if(username == null) throw new UserNotFoundException();
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public Student findStudentByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return user.getStudent();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public boolean userExistsByPhone(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean userExistsByNationalId(String nationalId) {
        return userRepository.findByNationalId(nationalId).isPresent();
    }

    public List<CourseSection> findCourseSectionByTerm(Term term) {
        return courseSectionRepository.findByTerm(term);
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

    public List<CourseSectionRegistration> findCourseSectionRegistrationByCourseSection(CourseSection courseSection) {
        return courseSectionRegistrationRepository
                .findByCourseSection(courseSection);
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



    public List<Instructor> findAllInstructors(int page, int size) {
        return instructorRepository.findAll(PageRequest.of(page, size)).toList();
    }

    public List<Instructor> findAllInstructors() {
        return Lists.newArrayList(instructorRepository.findAll());
    }

    public List<CourseSection> findCourseSectionByInstructor(Instructor instructor) {
        return courseSectionRepository.findByInstructor(instructor);
    }

    public void deleteInstructor(Instructor instructor) {
        instructorRepository.delete(instructor);
    }

    public CourseSectionRegistration findCourseSectionRegistrationByCourseSectionAndStudent(CourseSection courseSection, Student student) {
        return courseSectionRegistrationRepository
                .findByCourseSectionAndStudent(courseSection, student)
                .orElseThrow(CourseSectionRegistrationNotFoundException::new);
    }

    public List<Student> findAllStudents() {
        return Lists.newArrayList(studentRepository.findAll());
    }

    public List<CourseSectionRegistration> findCSRByStudent(Student student) {
        return courseSectionRegistrationRepository.findByStudent(student);
    }



    public List<Term> findAllTerms() {
        return Lists.newArrayList(termRepository.findAll());
    }

    public List<Term> findAllTerms(int page, int number) {
        return termRepository.findAll(PageRequest.of(page, number)).toList();
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
        return Lists.newArrayList(courseRepository.findAll());
    }

    public List<CourseSection> findAllCourseSections() {
        return Lists.newArrayList(courseSectionRepository.findAll());
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Course course) {
        courseRepository.delete(course);
    }

    public int findNumberOfStudentsInCourseSection(CourseSection courseSection) {
        return courseSectionRegistrationRepository.countByCourseSection(courseSection);
    }

    public List<User> findAllUsers() {
        return Lists.newArrayList(userRepository.findAll());
    }

    public List<User> findAllUsers(int page, int number) {
        return userRepository.findAll(PageRequest.of(page, number)).toList();
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


    public boolean courseSectionExistsByTerm(Term term, CourseSection courseSection) {
        return courseSectionRepository.existsByIdAndTerm(courseSection.getId(), term);
    }

    public boolean termExistsByTitle(String title) {
        return termRepository.findByTitle(title).isPresent();
    }

    public boolean csrExistsByCourseSectionAndStudent(CourseSection courseSection, Student student) {
        return courseSectionRegistrationRepository.findByCourseSectionAndStudent(courseSection, student).isPresent();
    }

    public boolean userExistsByAdminPrivilege() {
        return !userRepository.findByAdminTrue().isEmpty();
    }

    public List<User> findUserByAdminPrivilege() {
        return userRepository.findByAdminTrue();
    }

    public List<CourseSectionRegistration> findCSRsByStudentAndTerm(Student student, Term term) {
        List<CourseSectionRegistration> csrs = findCSRByStudent(student);
        List<CourseSection> courseSectionsWithSameTerm = findCourseSectionByTerm(term);
        return csrs.stream().filter(csr -> courseSectionsWithSameTerm
                .contains(csr.getCourseSection())).collect(Collectors.toList());
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void deleteStudent(Student student) {
        studentRepository.delete(student);
    }

    public Student findStudent(Long studentId) {
        return studentRepository.findById(studentId).orElseThrow(StudentNotFoundException::new);
    }

    public List<CourseSectionRegistration> findCourseSectionRegistrationByStudent(Student student) {
        return courseSectionRegistrationRepository.findByStudent(student);
    }

    public String saveToken(String username, String token) {
        tokenRepository.put(username, token);
        return token;
    }

    public void deleteTokenByUsername(String username) {
        tokenRepository.remove(username);
    }

    public boolean userExistsByToken(String token) {
        return tokenRepository.containsValue(token);
    }

    public String findUsernameByToken(String token) {
        return tokenRepository.keySet().stream().filter(key -> tokenRepository.get(key).equals(token)).findFirst()
                .orElseThrow(TokenNotFoundException::new);
    }

    public boolean tokenExistsByUsername(String username) {
        return tokenRepository.containsKey(username);
    }

    public boolean courseExistsByTitle(String title) {
        return courseRepository.existsByTitle(title);
    }

    public boolean userExistsByInstructor(Long instructorId) {
        return userRepository.existsByInstructorId(instructorId);
    }

    public boolean termExistsById(Long termId) {
        return termRepository.existsById(termId);
    }

    public boolean courseExistsById(Long courseId) {
        return courseRepository.existsById(courseId);
    }

    public boolean instructorExistsById(Long instructorId) {
        return instructorRepository.existsById(instructorId);
    }

    public Student findStudentByToken(String token) {
        String username = findUsernameByToken(token);
        return findStudentByUsername(username);
    }

    public List<Course> findAllCourses(int page, int number) {
        return courseRepository.findAll(PageRequest.of(page, number)).toList();
    }

    public List<CourseSection> findCourseSectionsByTermAndInstructorNameAndCourseTitle(Term term, String instructorName,
                                                                                       String courseName, PageRequest pageRequest) {
        return courseSectionRepository
                .findAllByTermAndInstructor_User_NameAndCourse_Title(term, instructorName, courseName, pageRequest);
    }
}
