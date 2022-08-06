package tech.sobhan.golestan.services;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.repositories.Repository;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class InstructorService {
    private final ErrorChecker errorChecker;
    private final Repository repository;

    public InstructorService(ErrorChecker errorChecker, Repository repository) {
        this.errorChecker = errorChecker;
        this.repository = repository;
    }


    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return list().toString();
    }

    private List<Instructor> list() {
        return repository.findAllInstructors();
    }

//    public Instructor create(Instructor instructor){
//        if (instructorExists(list(), instructor)) return null;
//        createLog(Instructor.class, instructor.getId());
//        return repository.saveInstructor(instructor);
//    }
//
//    private boolean instructorExists(List<Instructor> allInstructors, Instructor instructor) {
//        for (Instructor i : allInstructors) {
//            if(instructor.equals(i)){
//                System.out.println("ERROR403 duplicate Instructors");
//                return true;
//            }
//        }
//        return false;
//    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private Instructor read(Long instructorId) {
        return repository.findInstructor(instructorId);
    }

    public String update(Rank rank, Long instructorId, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        return update(rank, instructorId);
    }

    private String update(Rank rank, Long instructorId) {
        Instructor instructor = repository.findInstructor(instructorId);
        instructor.setRank(rank);
        repository.saveInstructor(instructor);
        return "OK";
    }

    public void delete(Long instructorId, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        delete(instructorId);
    }

    public void delete(Long instructorId) {
        Instructor instructor = repository.findInstructor(instructorId);
        List<CourseSection> courseSectionsOfInstructor = repository.findCourseSectionByInstructor(instructor);
        courseSectionsOfInstructor.forEach(cs -> cs.setInstructor(null));
        repository.deleteInstructor(instructor);
    }

    public String giveMark(String username, String password, Long courseSectionId, Long studentId, Double score) {
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSectionId);
        return giveMark(courseSectionId, studentId, score);
    }

    private String giveMark(Long courseSectionId, Long studentId, Double score) {
        CourseSection courseSection = repository.findCourseSection(courseSectionId);//todo move to security service
        Student student = repository.findStudent(studentId);
        CourseSectionRegistration courseSectionRegistration = repository
                .findCourseSectionRegistrationByCourseSectionAndStudent(courseSection, student);
        courseSectionRegistration.setScore(score);
        repository.saveCourseSectionRegistration(courseSectionRegistration);
        return "OK";
    }

    public String giveMultipleMarks(String username, String password, Long courseSectionId, JSONArray studentIds, JSONArray scores) {
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSectionId);
        int numberOfStudents = studentIds.length();
        int numberOfScores = scores.length();
        if(numberOfScores != numberOfStudents) throw new RuntimeException("sizes are not the same");
        IntStream.range(0,numberOfStudents).forEach(i -> {
            Long studentId = parseId(studentIds, i);
            Double score = parseScore(scores, i);
            giveMark(courseSectionId, studentId, score);
        });
        return "OK";
    }

    private Double parseScore(JSONArray scores, int i) {
        try{
            return Double.parseDouble(String.valueOf(scores.get(i)));
        }catch (JSONException j){
            j.printStackTrace();
            return null;
        }
    }

    private Long parseId(JSONArray studentIds, int i)  {
        try{
            return Long.parseLong(String.valueOf(studentIds.get(i)));
        }catch (JSONException j){
            j.printStackTrace();
            return null;
        }
    }
}
