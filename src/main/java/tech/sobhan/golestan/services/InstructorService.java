package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.repositories.RepositoryHandler;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;

@Slf4j
@Service
public class InstructorService {

    private final ErrorChecker errorChecker;
    private final RepositoryHandler repositoryHandler;

    public InstructorService(ErrorChecker errorChecker, RepositoryHandler repositoryHandler) {
        this.errorChecker = errorChecker;
        this.repositoryHandler = repositoryHandler;
    }


    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return list().toString();
    }

    private List<Instructor> list() {
        return repositoryHandler.findAllInstructors();
    }

    public Instructor create(Instructor instructor){
        if (instructorExists(list(), instructor)) return null;
        createLog(Instructor.class, instructor.getId());
        return repositoryHandler.saveInstructor(instructor);
    }

    private boolean instructorExists(List<Instructor> allInstructors, Instructor instructor) {
        for (Instructor i : allInstructors) {
            if(instructor.equals(i)){
                System.out.println("ERROR403 duplicate Instructors");
                return true;
            }
        }
        return false;
    }

    public String read(Long id, String username, String password) {//todo create service package
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private Instructor read(Long instructorId) {
        return repositoryHandler.findInstructor(instructorId);
    }

    public String update(Rank rank, Long instructorId, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        return update(rank, instructorId);
    }

    private String update(Rank rank, Long instructorId) {
        Instructor instructor = repositoryHandler.findInstructor(instructorId);
        instructor.setRank(rank);
        repositoryHandler.saveInstructor(instructor);
        return "OK";
    }

    public String delete(Long instructorId, String username, String password) {//todo check later
        errorChecker.checkIsAdmin(username, password);
        Instructor instructor = repositoryHandler.findInstructor(instructorId);
        List<CourseSection> courseSectionsOfInstructor = repositoryHandler.findCourseSectionByInstructor(instructorId);
        for (CourseSection courseSection : courseSectionsOfInstructor) {
            courseSection.setInstructor(null);
        }
        repositoryHandler.deleteInstructor(instructor);
        return "OK";
    }

    public String giveMark(String username, String password, Long courseSectionId, Long studentId, Double score) {
        CourseSection courseSection = repositoryHandler.findCourseSection(courseSectionId);
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSection);
        CourseSectionRegistration courseSectionRegistration = repositoryHandler
                .findCourseSectionRegistrationByCourseSectionAndStudent(courseSectionId, studentId);
        courseSectionRegistration.setScore(score);
        repositoryHandler.saveCourseSectionRegistration(courseSectionRegistration);
        return "OK";
    }

    public String giveMultipleMark(String username, String password, Long courseSectionId, List<Long> studentIds, List<Double> scores) {
        if(studentIds.size() != scores.size()) throw new RuntimeException("sizes are not the same");
        for (int i=0;i<studentIds.size();i++) {//todo change to map
            giveMark(username, password, courseSectionId, studentIds.get(i), scores.get(i));//todo remove username and password from here
        }
        return "OK";
    }
}
