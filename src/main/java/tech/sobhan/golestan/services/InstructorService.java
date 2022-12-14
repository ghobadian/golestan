package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Instructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstructorService {
    private final Repo repo;

    public List<Instructor> list(int page, int number) {
        return repo.findAllInstructors(page, number);
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
        List<CourseSection> courseSectionsOfInstructor = repo.findCourseSectionByInstructorId(instructorId);
        courseSectionsOfInstructor.forEach(cs -> cs.setInstructor(null));
        repo.deleteInstructor(instructorId);
        log.info("Instructor with id " + instructorId + " deleted");
    }

    public CourseSectionRegistration giveMark(Long courseSectionId, Long studentId, Double score) {
        CourseSectionRegistration csr = repo
                .findCourseSectionRegistrationByCourseSectionIdAndStudentId(courseSectionId, studentId);
        csr.setScore(score);
        return repo.saveCourseSectionRegistration(csr);
    }

    public List<CourseSectionRegistration> giveMultipleMarks(Long courseSectionId, JSONArray studentIds, JSONArray scores) {
        int numberOfStudents = studentIds.length();
        int numberOfScores = scores.length();
        if (numberOfScores != numberOfStudents) throw new RuntimeException("sizes are not the same");
        List<CourseSectionRegistration> response = new ArrayList<>();
        IntStream.range(0,numberOfStudents).forEach(i -> giveSingleMark(courseSectionId, studentIds, scores, response, i));
        return response;
    }

    private void giveSingleMark(Long courseSectionId, JSONArray studentIds, JSONArray scores,
                                List<CourseSectionRegistration> response, int i) {
        Long studentId = Long.parseLong(String.valueOf(studentIds.get(i)));
        Double score = Double.parseDouble(String.valueOf(scores.get(i)));
        response.add(giveMark(courseSectionId, studentId, score));
    }
}
