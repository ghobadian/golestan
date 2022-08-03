package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.*;
import tech.sobhan.golestan.business.exceptions.duplication.CourseSectionDuplicationException;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.repositories.RepositoryHandler;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;

@Service
@Slf4j
public class CourseSectionService {
    private final RepositoryHandler repositoryHandler;
    private final ErrorChecker errorChecker;

    public CourseSectionService(RepositoryHandler repositoryHandler, ErrorChecker errorChecker) {
        this.repositoryHandler = repositoryHandler;
        this.errorChecker = errorChecker;
    }
    public String list(Long termId, String username, String password, String instructorName, String courseName, Integer pageNumber, Integer maxInEachPage) {//todo change to list and methods likewise
        errorChecker.checkIsUser(username, password);
        List<CourseSection> filteredList = list(termId, instructorName, courseName);
        List<CourseSection> paginationList = pagination(filteredList, pageNumber, maxInEachPage);
        return paginationList.toString();
    }

    private List<CourseSection> pagination(List<CourseSection> filteredList, Integer pageNumber, Integer maxInEachPage) {
        final Integer DEFAULT_MAX_IN_EACH_PAGE = 5;//todo move to constants
        if(pageNumber == null && maxInEachPage==null){
            return filteredList;
        }else if(pageNumber != null && maxInEachPage != null){
            errorChecker.checkPageLength(filteredList.size(), pageNumber, maxInEachPage);
            return filteredList.subList((pageNumber - 1)  * maxInEachPage, (pageNumber)  * maxInEachPage);
        }else if(pageNumber!= null && maxInEachPage == null){
            errorChecker.checkPageLength(filteredList.size(), pageNumber, DEFAULT_MAX_IN_EACH_PAGE);
            return filteredList.subList((pageNumber - 1)  * DEFAULT_MAX_IN_EACH_PAGE, (pageNumber)  * DEFAULT_MAX_IN_EACH_PAGE);
        }else{
            throw new NullPointerException();//todo change to custom exception
        }
    }

    private List<CourseSection> list(Long termId, String instructorName, String courseName) {//todo test
        if(instructorName!=null && courseName == null){
            return repositoryHandler.findCourseSectionByInstructorName(instructorName);
        }else if(instructorName==null && courseName!= null){
            return repositoryHandler.findCourseSectionByCourseName(courseName);
        }else if(instructorName!=null && courseName!= null){
            return repositoryHandler.findCourseSectionByInstructorNameAndCourseName(instructorName, courseName);
        }else{
            return list(termId);
        }
    }

    private List<CourseSection> list(Long termId) {
        return repositoryHandler.findCourseSectionByTerm(termId);
    }

    public String create(Long courseId, Long instructorId, Long termId, String username, String password) {
        CourseSection courseSection = buildCourseSection(courseId, instructorId, termId);
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSection);
        checkCourseSectionExists(termId, courseSection);//todo move to error checker
        return create(courseSection).toString();
    }

    private CourseSection buildCourseSection(Long courseId, Long instructorId, Long termId) {
        Course course = repositoryHandler.findCourse(courseId);
        Instructor instructor = repositoryHandler.findInstructor(instructorId);
        Term term = repositoryHandler.findTerm(termId);
        return CourseSection.builder().instructor(instructor).course(course).term(term).build();
    }

    public CourseSection create(CourseSection courseSection) {
        createLog(CourseSection.class, courseSection.getId());
        return repositoryHandler.saveCourseSection(courseSection);
    }

    private void checkCourseSectionExists(Long termId, CourseSection courseSection) {
        List<CourseSection> allCourseSections;
        try{            //todo important
            allCourseSections = list(termId);
        }catch(CourseSectionNotFoundException e){
            return;
        }
        for (CourseSection cs : allCourseSections) {
            if(courseSection.equals(cs)){
                throw new CourseSectionDuplicationException();
            }
        }
    }

    public String read(Long id, String username, String password) throws JSONException {
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private JSONObject read(Long id) throws JSONException {
        CourseSection courseSection = repositoryHandler.findCourseSection(id);
        int numberOfStudents = repositoryHandler.findNumberOfStudentsInCourseSection(courseSection.getId());
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
