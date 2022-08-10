package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.InstructorService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorSecurityService {
    private final InstructorService service;
    private final ErrorChecker errorChecker;

    public List<Instructor> list(String token) {
        errorChecker.checkIsUser(token);
        return service.list();
    }

    public Instructor read(Long id, String token) {
        errorChecker.checkIsUser(token);
        return service.read(id);
    }

    public Instructor update(Rank rank, Long instructorId, String token) {
        errorChecker.checkIsAdmin(token);
        return service.update(rank, instructorId);
    }

    public void delete(Long instructorId, String token) {
        errorChecker.checkIsAdmin(token);
        service.delete(instructorId);
    }

    public CourseSectionRegistration giveMark(String token, Long courseSectionId, Long studentId, Double score) {
        errorChecker.checkIsInstructorOfCourseSection(token, courseSectionId);
        return service.giveMark(courseSectionId, studentId, score);
    }

    public List<CourseSectionRegistration> giveMultipleMarks(String token, Long courseSectionId, JSONArray studentIds, JSONArray scores) {
        errorChecker.checkIsInstructorOfCourseSection(token, courseSectionId);
        return service.giveMultipleMarks(courseSectionId, studentIds, scores);
    }
}
