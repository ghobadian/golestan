package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.dao.Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstructorService {
    private final Repo repo;

    public List<Instructor> list() {
        return repo.findAllInstructors();
    }

    public Instructor read(Long instructorId) {
        return repo.findInstructor(instructorId);
    }

    public Instructor update(Rank rank, Long instructorId) {
        Instructor instructor = repo.findInstructor(instructorId);
        instructor.setRank(rank);
        return repo.saveInstructor(instructor);
    }

    public void delete(Long instructorId) {
        Instructor instructor = repo.findInstructor(instructorId);
        List<CourseSection> courseSectionsOfInstructor = repo.findCourseSectionByInstructor(instructor);
        courseSectionsOfInstructor.forEach(cs -> cs.setInstructor(null));
        repo.deleteInstructor(instructor);
        log.info("Instructor with id " + instructor.getId() + " deleted");
    }

    public CourseSectionRegistration giveMark(Long courseSectionId, Long studentId, Double score) {
        CourseSection courseSection = repo.findCourseSection(courseSectionId);
        Student student = repo.findStudent(studentId);
        CourseSectionRegistration courseSectionRegistration = repo
                .findCourseSectionRegistrationByCourseSectionAndStudent(courseSection, student);
        courseSectionRegistration.setScore(score);
        return repo.saveCourseSectionRegistration(courseSectionRegistration);
    }

    public List<CourseSectionRegistration> giveMultipleMarks(Long courseSectionId, JSONArray studentIds, JSONArray scores) {
        int numberOfStudents = studentIds.length();
        int numberOfScores = scores.length();
        if(numberOfScores != numberOfStudents) throw new RuntimeException("sizes are not the same");
        List<CourseSectionRegistration> response = new ArrayList<>();
        IntStream.range(0,numberOfStudents).forEach(i -> {
            Long studentId = parseId(studentIds, i);
            Double score = parseScore(scores, i);
            response.add(giveMark(courseSectionId, studentId, score));
        });
        return response;
    }

    private Double parseScore(JSONArray scores, int i) {
        try{
            return Double.parseDouble(String.valueOf(scores.get(i)));
        }catch (JSONException j) {
            j.printStackTrace();
            return null;
        }
    }

    private Long parseId(JSONArray studentIds, int i)  {
        try{
            return Long.parseLong(String.valueOf(studentIds.get(i)));
        }catch (JSONException j) {
            j.printStackTrace();
            return null;
        }
    }
}
