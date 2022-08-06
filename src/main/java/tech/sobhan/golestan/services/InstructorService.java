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

import java.util.List;
import java.util.stream.IntStream;

@Service
public class InstructorService {
    private final Repository repository;

    public InstructorService(Repository repository) {
        this.repository = repository;
    }

    public List<Instructor> list() {
        return repository.findAllInstructors();
    }

    public Instructor read(Long instructorId) {
        return repository.findInstructor(instructorId);
    }



    public String update(Rank rank, Long instructorId) {
        Instructor instructor = repository.findInstructor(instructorId);
        instructor.setRank(rank);
        repository.saveInstructor(instructor);
        return "OK";
    }

    public void delete(Long instructorId) {
        Instructor instructor = repository.findInstructor(instructorId);
        List<CourseSection> courseSectionsOfInstructor = repository.findCourseSectionByInstructor(instructor);
        courseSectionsOfInstructor.forEach(cs -> cs.setInstructor(null));
        repository.deleteInstructor(instructor);
    }

    public String giveMark(Long courseSectionId, Long studentId, Double score) {
        CourseSection courseSection = repository.findCourseSection(courseSectionId);//todo move to security service
        Student student = repository.findStudent(studentId);
        CourseSectionRegistration courseSectionRegistration = repository
                .findCourseSectionRegistrationByCourseSectionAndStudent(courseSection, student);
        courseSectionRegistration.setScore(score);
        repository.saveCourseSectionRegistration(courseSectionRegistration);
        return "OK";
    }

    public String giveMultipleMarks(Long courseSectionId, JSONArray studentIds, JSONArray scores) {
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
