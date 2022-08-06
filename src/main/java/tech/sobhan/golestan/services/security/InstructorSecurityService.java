package tech.sobhan.golestan.services.security;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.InstructorService;

@Service
public class InstructorSecurityService {
    private final InstructorService service;
    private final ErrorChecker errorChecker;

    public InstructorSecurityService(InstructorService service, ErrorChecker errorChecker) {
        this.service = service;
        this.errorChecker = errorChecker;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return service.list().toString();
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return service.read(id).toString();
    }

    public String update(Rank rank, Long instructorId, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        return service.update(rank, instructorId);
    }

    public void delete(Long instructorId, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        service.delete(instructorId);
    }

    public String giveMark(String username, String password, Long courseSectionId, Long studentId, Double score) {
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSectionId);
        return service.giveMark(courseSectionId, studentId, score);
    }

    public String giveMultipleMarks(String username, String password, Long courseSectionId, JSONArray studentIds, JSONArray scores) {
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSectionId);
        return service.giveMultipleMarks(courseSectionId, studentIds, scores);
    }
}
