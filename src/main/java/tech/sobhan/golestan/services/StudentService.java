package tech.sobhan.golestan.services;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.notFound.StudentNotFoundException;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.Repository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final Repository repository;

    public StudentService(Repository repository) {
        this.repository = repository;
    }

//    public List<Student> list() {//todo find usage
//        return repository.findAllStudents();
//    }
    public String signUpSection(Student student, CourseSection courseSection) {
        CourseSectionRegistration csr = CourseSectionRegistration.builder()
                .student(student).courseSection(courseSection).build();
        repository.saveCourseSectionRegistration(csr);
        return "Successfully signed up for course section.";
    }

    public JSONArray seeScoresInSpecifiedTerm(Long termId, String username) {
        Student student = repository.findStudentByUsername(username);
        Term term = repository.findTerm(termId);
        List<CourseSectionRegistration> csrs = repository.findCSRsByStudentAndTerm(student, term);
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


    public String seeSummery(String username) {
        User user = repository.findUserByUsername(username);
        Student student = Optional.ofNullable(user.getStudent()).orElseThrow(StudentNotFoundException::new);
        double totalSum = 0;

        List<Term> terms = repository.findAllTerms();

        JSONArray output = new JSONArray();
        for (Term term : terms) {
            JSONObject termDetails = new JSONObject();
            double averageInSpecifiedTerm = averageInSpecifiedTerm(term, student);
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

    private double averageInSpecifiedTerm(Term term, Student studentId) {
        List<CourseSectionRegistration> courseSectionRegistrations =
                repository.findCSRsByStudentAndTerm(studentId, term);
        return courseSectionRegistrations.isEmpty() ? 0 : findAverage(courseSectionRegistrations);
    }

    public String listCourseSectionStudents(List<CourseSectionRegistration> courseSectionRegistrations) {
        JSONArray output = new JSONArray();
        for (CourseSectionRegistration courseSectionRegistration : courseSectionRegistrations) {
            Student student = courseSectionRegistration.getStudent();
            User user = repository.findUserByStudent(student.getId());
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

    public void delete(Long studentId) {
        Student student = repository.findStudent(studentId);
        List<CourseSectionRegistration> csrs = repository.findCourseSectionRegistrationByStudent(student);
        csrs.forEach(csr -> csr.setStudent(null));
        repository.deleteStudent(student);
    }
}