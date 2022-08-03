package tech.sobhan.golestan.services;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.business.exceptions.AlreadySignedUpException;
import tech.sobhan.golestan.business.exceptions.CourseSectionRegistrationNotFoundException;
import tech.sobhan.golestan.business.exceptions.StudentNotFoundException;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.repositories.RepositoryHandler;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static tech.sobhan.golestan.utils.Util.createLog;

@Service
@Slf4j
public class StudentService {//todo clean this class
    private final RepositoryHandler repositoryHandler;
    private final ErrorChecker errorChecker;

    public StudentService(RepositoryHandler repositoryHandler, ErrorChecker errorChecker) {
        this.repositoryHandler = repositoryHandler;
        this.errorChecker = errorChecker;
    }

    public Student create(Student student){
        if (studentExists(list(), student)) return null;
        createLog(Student.class, student.getStudentId());
        return repositoryHandler.saveStudent(student);
    }
    private boolean studentExists(List<Student> allStudents, Student student) {
        for (Student s : allStudents) {
            if(student.equals(s)){
                System.out.println("ERROR403 duplicate Students");
                return true;
            }
        }
        return false;
    }
    public List<Student> list() {
        return repositoryHandler.findAllStudents();
    }
    public String signUpSection(Long course_section_id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        CourseSection courseSection = findCourseSection(course_section_id);
        Student student = findStudent(username);
        createAndSaveCourseSectionRegistration(courseSection, student);
        return "Successfully signed up for course section.";
    }

    private CourseSection findCourseSection(Long courseSectionId) {
        return repositoryHandler.findCourseSection(courseSectionId);
    }

    private void createAndSaveCourseSectionRegistration(CourseSection courseSection, Student student) {
        if(alreadySignedUp(courseSection.getId(), student.getStudentId())) throw new AlreadySignedUpException();
        CourseSectionRegistration courseSectionRegistration = CourseSectionRegistration.builder()
                .student(student).courseSection(courseSection).build();
        repositoryHandler.saveCourseSectionRegistration(courseSectionRegistration);
    }

    private boolean alreadySignedUp(Long courseSectionId, Long studentId) {//todo do sth for already signed ups and likewise
        try{
            return repositoryHandler.findCourseSectionRegistrationByCourseSectionAndStudent(courseSectionId, studentId)!=null;
        }catch (CourseSectionRegistrationNotFoundException e){
            return false;
        }
    }
    private Student findStudent(String username) {
        return repositoryHandler.findStudentByUsername(username);
    }
    public JSONArray seeScoresInSpecifiedTerm(Long term_id, String username, String password) throws JSONException {
        errorChecker.checkIsUser(username, password);
        Long studentId = findStudent(username).getStudentId();
        List<CourseSectionRegistration> courseSectionRegistrations =
                findCourseSectionRegistrationsOfSpecifiedStudentAndTerm(studentId, term_id);

        double avg = findAverage(courseSectionRegistrations);//todo emtiazi
        JSONArray output = new JSONArray();
        output.put(new JSONObject("{average:"+avg+"}"));
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            CourseSection courseSection = courseSectionRegistration.getCourseSection();
            Course course = courseSection.getCourse();
            JSONObject courseSectionDetails = getCourseSectionDetails(courseSectionRegistration, courseSection, course);
            output.put(courseSectionDetails);
        }
        return output;
    }

    private JSONObject getCourseSectionDetails(CourseSectionRegistration courseSectionRegistration, CourseSection courseSection, Course course) throws JSONException {
        JSONObject courseSectionDetails = new JSONObject();
        courseSectionDetails.put("course_section_id", courseSection.getId());
        courseSectionDetails.put("course_name", course.getTitle());
        courseSectionDetails.put("course_units", course.getUnits());
        courseSectionDetails.put("instructor", courseSection.getInstructor());
        courseSectionDetails.put("score", courseSectionRegistration.getScore());
        return courseSectionDetails;
    }

    private List<CourseSectionRegistration> findCourseSectionRegistrationsOfSpecifiedStudentAndTerm(Long studentId, Long termId) {
        List<CourseSectionRegistration> courseSectionRegistrations = repositoryHandler
                .findCourseSectionRegistrationByStudent(studentId);
        List<CourseSection> courseSections = repositoryHandler.findCourseSectionByTerm(termId);
        Set<CourseSectionRegistration> output = new HashSet<>();
        filterCourseSectionRegistrationsOfSpecifiedTerm(courseSectionRegistrations, courseSections, output);
        return output.stream().toList();
    }
    private void filterCourseSectionRegistrationsOfSpecifiedTerm(List<CourseSectionRegistration> courseSectionRegistrations, List<CourseSection> courseSections, Set<CourseSectionRegistration> output) {
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            for (CourseSection courseSection : courseSections) {
                if(courseSection.equals(courseSectionRegistration.getCourseSection())){
                    output.add(courseSectionRegistration);
                }
            }
        }
    }
    private double findAverage(List<CourseSectionRegistration> courseSectionRegistrations) {
        double sum = 0;
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            sum += courseSectionRegistration.getScore();
        }
        return sum / courseSectionRegistrations.size();
    }
    public String seeSummery(String username, String password) throws JSONException {
        errorChecker.checkIsUser(username, password);
        User user = repositoryHandler.findUserByUsername(username);
        Student student = Optional.ofNullable(user.getStudent()).orElseThrow(StudentNotFoundException::new);
        double totalSum = 0;

        List<Term> terms = repositoryHandler.findAllTerms();

        JSONArray output = new JSONArray();
        for (Term term : terms) {
            JSONObject termDetails = new JSONObject();
            double averageInSpecifiedTerm = averageInSpecifiedTerm(term.getId(), student.getStudentId());
            findTermDetails(term, termDetails, averageInSpecifiedTerm);
            totalSum += averageInSpecifiedTerm;
            output.put(termDetails);
        }

        addTotalAverage(totalSum, terms, output);
        return output.toString();
    }

    private void addTotalAverage(double totalSum, List<Term> terms, JSONArray output) throws JSONException {
        double totalAverage = totalSum / terms.size();
        output.put(new JSONObject("{totalAverage:"+totalAverage+"}"));
    }

    private void findTermDetails(Term term, JSONObject termDetails, double averageInSpecifiedTerm) throws JSONException {
        termDetails.put("term_id", term.getId());
        termDetails.put("term_title", term.getTitle());
        termDetails.put("term", term.getTitle());
        termDetails.put("average", averageInSpecifiedTerm);
    }

    private double averageInSpecifiedTerm(Long termId, Long studentId) {
        List<CourseSectionRegistration> courseSectionRegistrations =
                findCourseSectionRegistrationsOfSpecifiedStudentAndTerm(studentId, termId);
        return courseSectionRegistrations.isEmpty() ? 0 : findAverage(courseSectionRegistrations);
    }

    @SneakyThrows
    public String listCourseSectionStudents(Long courseSectionId, String username, String password) {
        CourseSection courseSection = repositoryHandler.findCourseSection(courseSectionId);
        errorChecker.checkIsInstructorOfCourseSectionOrAdmin(username, password, courseSection);
        List<CourseSectionRegistration> courseSectionRegistrations = repositoryHandler
                .findCourseSectionRegistrationByCourseSection(courseSectionId);
        JSONArray output = new JSONArray();
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            Student student = courseSectionRegistration.getStudent();
            User user = repositoryHandler.findUserByStudent(student.getStudentId());
            JSONObject studentDetails = new JSONObject();
            studentDetails.put("studentId", student.getStudentId());
            studentDetails.put("studentName", user.getName());
            studentDetails.put("studentNumber", student.getStudentId());
            studentDetails.put("score", courseSectionRegistration.getScore());
            output.put(studentDetails);
        }
        return output.toString();
    }
}