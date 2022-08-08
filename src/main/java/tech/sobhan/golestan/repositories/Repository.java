package tech.sobhan.golestan.repositories;

import tech.sobhan.golestan.business.exceptions.notFound.*;
import tech.sobhan.golestan.models.*;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Repository
public class Repository {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final TermRepository termRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    private final TokenRepository tokenRepository;

    public Repository(UserRepository userRepository,
                      StudentRepository studentRepository, CourseRepository courseRepository,
                      InstructorRepository instructorRepository,
                      TermRepository termRepository,
                      CourseSectionRepository courseSectionRepository,
                      CourseSectionRegistrationRepository courseSectionRegistrationRepository,
                      TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.termRepository = termRepository;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
        this.tokenRepository = tokenRepository;
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public List<CourseSection> findCourseSectionByTerm(Term term){
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
        return studentRepository.findAll();
    }

    public Student findStudentByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return user.getStudent();
    }

    public List<CourseSectionRegistration> findCSRByStudent(Student student) {
        return courseSectionRegistrationRepository.findByStudent(student);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public boolean userExistsByPhone(String phone){
        return userRepository.findByPhone(phone).isPresent();
    }

    public boolean userExistsByUsername(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean userExistsByNationalId(String nationalId){
        return userRepository.findByNationalId(nationalId).isPresent();
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

    public int findNumberOfStudentsInCourseSection(CourseSection courseSection) {
        return courseSectionRegistrationRepository.countByCourseSection(courseSection);
    }

    public List<CourseSection> findCourseSectionByInstructorName(String instructorName) {
        List<CourseSection> allCourseSections = findAllCourseSections();
        List<CourseSection> output = new ArrayList<>();
        for (CourseSection courseSection : allCourseSections) {
            Long currentCourseSectionInstructorId = courseSection.getInstructor().getId();
            String currentCourseSectionInstructorName;
            try{
                currentCourseSectionInstructorName = findUserByInstructor(currentCourseSectionInstructorId).getName();
            }catch (UserNotFoundException u){
                continue;
            }
            if(instructorName.equals(currentCourseSectionInstructorName)){
                output.add(courseSection);
            }
        }
        return output;
    }

    public List<CourseSection> findCourseSectionByCourseName(String courseTitle) {
        return courseSectionRepository.findByCourse_Title(courseTitle);
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


    public boolean courseSectionExistsByTerm(Term term, CourseSection courseSection) {
        return courseSectionRepository.findByTerm(term).stream().anyMatch(cs -> cs.equals(courseSection));
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

    public void deleteUsersWithAdminPrivilege() {
        userRepository.deleteAllInBatch(userRepository.findByAdminTrue());
    }

    public List<CourseSectionRegistration> findCSRsByStudentAndTerm(Student student, Term term) {
        List<CourseSectionRegistration> csrs = findCSRByStudent(student);
        List<CourseSection> courseSections = findCourseSectionByTerm(term);
        Set<CourseSectionRegistration> output = new HashSet<>();
        filterCSRsOfSpecifiedTerm(csrs, courseSections, output);
        return new ArrayList<>(output);
    }

    private static void filterCSRsOfSpecifiedTerm(List<CourseSectionRegistration> courseSectionRegistrations, List<CourseSection> courseSections, Set<CourseSectionRegistration> output) {
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {//todo use set and retainAll
            for (CourseSection courseSection : courseSections) {
                if(courseSection.equals(courseSectionRegistration.getCourseSection())){
                    output.add(courseSectionRegistration);
                }
            }
        }
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

    public Token saveToken(Token token){
        return tokenRepository.save(token);
    }

    public Token findToken(String username){
        return tokenRepository.findById(username).orElseThrow(UserNotFoundException::new);
    }

    public void deleteToken(Token token){
        tokenRepository.delete(token);
    }


    public boolean userExistsByToken(String token) {
        return tokenRepository.existsByToken(token);
    }

    public Token findTokenByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public boolean tokenExistsByUsername(String username) {
        return tokenRepository.existsByUsername(username);
    }

    public boolean courseExistsByTitle(String title) {
        return courseRepository.existsByTitle(title);
    }

    public List<Token> findAllTokens(){
        return tokenRepository.findAll();
    }
}
