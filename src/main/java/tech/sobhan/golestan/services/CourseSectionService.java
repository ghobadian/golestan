package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.*;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.repositories.*;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.*;

@Service
@Slf4j
public class CourseSectionService {
    private final RepositoryHandler repositoryHandler;
    private final ErrorChecker errorChecker;

    public CourseSectionService(RepositoryHandler repositoryHandler, ErrorChecker errorChecker) {
        this.repositoryHandler = repositoryHandler;
        this.errorChecker = errorChecker;
    }
    public String list(Long termId, String username, String password) {
        errorChecker.checkIsUser(username, password);
        List<CourseSection> output = list(termId);
        return output.isEmpty() ? "ERROR 404" : output.toString();
    }

    private List<CourseSection> list(Long termId) {
        return repositoryHandler.findCourseSectionByTerm(termId);
    }

    public String create(Long courseId, Long instructorId, Long termId, String username, String password) {
        Course course = repositoryHandler.findCourse(courseId);
        Instructor instructor = repositoryHandler.findInstructor(instructorId);
        Term term = repositoryHandler.findTerm(termId);
        CourseSection courseSection = CourseSection.builder().instructor(instructor).course(course).term(term).build();
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSection);
        if(courseSectionExists(list(term.getId()), courseSection)) return "ERROR course section already exists";//todo move to error checker
        return create(courseSection).toString();
    }

    public CourseSection create(CourseSection courseSection) {
        createLog(CourseSection.class, courseSection.getId());
        return repositoryHandler.saveCourseSection(courseSection);
    }


    private boolean courseSectionExists(List<CourseSection> allCourseSections, CourseSection courseSection) {
        for (CourseSection cs : allCourseSections) {
            if(courseSection.equals(cs)){
                System.out.println("ERROR403 duplicate CourseSections");
                return true;
            }
        }
        return false;
    }

    public String read(Long id, String username, String password) throws JSONException {
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private JSONObject read(Long id) throws JSONException {
        CourseSection courseSection = repositoryHandler.findCourseSection(id);
        int numberOfStudents = repositoryHandler.findCourseSectionRegistrationByCourseSection(courseSection.getId()).size();
        JSONObject output = new JSONObject();
        output.put("courseSection", courseSection);
        output.put("numberOfStudents",numberOfStudents);
        return output;
    }


    public String update(Long termId, Long courseId, Long instructorId, Long courseSectionId, String username, String password) {
        CourseSection courseSection = repositoryHandler.findCourseSection(courseSectionId);
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSection);
        return update(termId, courseId, instructorId, courseSectionId);
    }

    private String update(Long termId, Long courseId, Long instructorId, Long courseSectionId) {
        CourseSection courseSection = repositoryHandler.findCourseSection(courseSectionId);
        try{
            Term term = repositoryHandler.findTerm(termId);
            courseSection.setTerm(term);
        }catch (TermNotFoundException ignored){
        }
        try{
            Course course = repositoryHandler.findCourse(courseId);
            courseSection.setCourse(course);
        }catch (CourseNotFoundException ignored){
        }
        try{
            Instructor instructor = repositoryHandler.findInstructor(instructorId);
            courseSection.setInstructor(instructor);
        }catch (InstructorNotFoundException ignored){
        }
        repositoryHandler.saveCourseSection(courseSection);
        return "OK";
//
//                .map(courseSection -> {
//                    courseSection = newCourseSection.clone();
//                    return courseSectionRepository.save(courseSection);//todo sus for saveUser instead of user
//                }).orElseGet(() -> {
//                    newCourseSection.setId(id);
//                    return courseSectionRepository.save(newCourseSection.clone());
//                });
    }

    public String delete(Long courseSectionId, String username, String password) throws JSONException, CourseSectionRegistrationNotEmptyException {
        CourseSection courseSection = repositoryHandler.findCourseSection(courseSectionId);
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSection);
        try{
            repositoryHandler.findCourseSectionRegistrationByCourseSection(courseSectionId);
            throw new CourseSectionRegistrationNotEmptyException();
        }catch(CourseSectionRegistrationNotFoundException e){
            repositoryHandler.deleteCourseSection(courseSection);
            return "OK";
        }
    }
}
