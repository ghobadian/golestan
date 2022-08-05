package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.StudentNotFoundException;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.RepositoryHandler;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StudentService {
    private final RepositoryHandler repositoryHandler;
    private final ErrorChecker errorChecker;

    public StudentService(RepositoryHandler repositoryHandler, ErrorChecker errorChecker) {
        this.repositoryHandler = repositoryHandler;
        this.errorChecker = errorChecker;
    }

    public List<Student> list() {
        return repositoryHandler.findAllStudents();
    }
    public String signUpSection(Long courseSectionId, String username, String password) {
        errorChecker.checkIsUser(username, password);
        CourseSection courseSection = repositoryHandler.findCourseSection(courseSectionId);
        Student student = repositoryHandler.findStudentByUsername(username);
        createAndSaveCourseSectionRegistration(courseSection, student);
        return "Successfully signed up for course section.";
    }

    private void createAndSaveCourseSectionRegistration(CourseSection courseSection, Student student) {
        errorChecker.checkCourseSectionRegistrationExists(courseSection.getId(), student.getId());
        CourseSectionRegistration csr = CourseSectionRegistration.builder().student(student).courseSection(courseSection).build();
        repositoryHandler.saveCourseSectionRegistration(csr);
    }

    public JSONArray seeScoresInSpecifiedTerm(Long term_id, String username, String password){
        errorChecker.checkIsUser(username, password);
        Long studentId = repositoryHandler.findStudentByUsername(username).getId();
        List<CourseSectionRegistration> csrs = repositoryHandler.findCSRsByStudentAndTerm(studentId, term_id);
        JSONObject average = toJson(findAverage(csrs));//todo emtiazi
        JSONArray output = new JSONArray();
        if(average!=null)   output.put(average);
        csrs.forEach(csr -> output.put(getCourseSectionDetails(csr)));
        return output;
    }

    private JSONObject toJson(double average) {
        try{
            return new JSONObject("{average:"+average+"}");
        }catch (JSONException j){
            j.printStackTrace();
            return null;
        }
    }

    private JSONObject getCourseSectionDetails(CourseSectionRegistration courseSectionRegistration){
        CourseSection courseSection = courseSectionRegistration.getCourseSection();
        Course course = courseSection.getCourse();
        JSONObject courseSectionDetails = new JSONObject();
        try{
            courseSectionDetails.put("course_section_id", courseSection.getId());
            courseSectionDetails.put("course_name", course.getTitle());
            courseSectionDetails.put("course_units", course.getUnits());
            courseSectionDetails.put("instructor", courseSection.getInstructor());
            courseSectionDetails.put("score", courseSectionRegistration.getScore());
        }catch (JSONException j){
            j.printStackTrace();
        }
        return courseSectionDetails;
    }

    private double findAverage(List<CourseSectionRegistration> courseSectionRegistrations) {
        double sum = 0;
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            sum += courseSectionRegistration.getScore();
        }
        return sum / courseSectionRegistrations.size();
    }
    public String seeSummery(String username, String password) {
        errorChecker.checkIsUser(username, password);
        User user = repositoryHandler.findUserByUsername(username);
        Student student = Optional.ofNullable(user.getStudent()).orElseThrow(StudentNotFoundException::new);
        double totalSum = 0;

        List<Term> terms = repositoryHandler.findAllTerms();

        JSONArray output = new JSONArray();
        for (Term term : terms) {
            JSONObject termDetails = new JSONObject();
            double averageInSpecifiedTerm = averageInSpecifiedTerm(term.getId(), student.getId());
            findTermDetails(term, termDetails, averageInSpecifiedTerm);
            totalSum += averageInSpecifiedTerm;
            output.put(termDetails);
        }
        addTotalAverage(totalSum, terms.size(), output);
        return output.toString();
    }

    private void addTotalAverage(double totalSum, int numberOfTerms, JSONArray output) {
        double totalAverage = totalSum / numberOfTerms;
        try{
            output.put(new JSONObject("{totalAverage:"+totalAverage+"}"));
        }catch (JSONException j){
            j.printStackTrace();
        }
    }

    private void findTermDetails(Term term, JSONObject termDetails, double averageInSpecifiedTerm) {
        try{
            termDetails.put("term_id", term.getId());
            termDetails.put("term", term.getTitle());
            termDetails.put("average", averageInSpecifiedTerm);
        }catch (JSONException j){
            j.printStackTrace();
        }
    }

    private double averageInSpecifiedTerm(Long termId, Long studentId) {
        List<CourseSectionRegistration> courseSectionRegistrations =
                repositoryHandler.findCSRsByStudentAndTerm(studentId, termId);
        return courseSectionRegistrations.isEmpty() ? 0 : findAverage(courseSectionRegistrations);
    }

    public String listCourseSectionStudents(Long courseSectionId, String username, String password) {
        CourseSection courseSection = repositoryHandler.findCourseSection(courseSectionId);
        errorChecker.checkIsInstructorOfCourseSectionOrAdmin(username, password, courseSection);
        List<CourseSectionRegistration> courseSectionRegistrations = repositoryHandler
                .findCourseSectionRegistrationByCourseSection(courseSectionId);
        JSONArray output = new JSONArray();
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            Student student = courseSectionRegistration.getStudent();
            User user = repositoryHandler.findUserByStudent(student.getId());
            JSONObject studentDetails = getStudentDetails(courseSectionRegistration, student, user);
            output.put(studentDetails);
        }
        return output.toString();
    }

    private JSONObject getStudentDetails(CourseSectionRegistration courseSectionRegistration, Student student, User user){
        JSONObject studentDetails = new JSONObject();
        try{
            studentDetails.put("studentId", student.getId());
            studentDetails.put("studentName", user.getName());
            studentDetails.put("studentNumber", student.getId());
            studentDetails.put("score", courseSectionRegistration.getScore());
        }catch (JSONException j){
            j.printStackTrace();
        }

        return studentDetails;
    }
}