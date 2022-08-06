package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.notFound.CourseNotFoundException;
import tech.sobhan.golestan.business.exceptions.notFound.InstructorNotFoundException;
import tech.sobhan.golestan.business.exceptions.notFound.TermNotFoundException;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.repositories.RepositoryHandler;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.constants.Etc.DEFAULT_MAX_IN_EACH_PAGE;
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
    public String list(Long termId, String username, String password, String instructorName,
                       String courseName, Integer pageNumber, Integer maxInEachPage) {
        errorChecker.checkIsUser(username, password);
        List<CourseSection> filteredList = filterList(termId, instructorName, courseName);
        List<CourseSection> paginationList = pagination(filteredList, pageNumber, maxInEachPage);
        JSONArray output = listToJsonArray(paginationList);
        return output.toString();
    }

    private JSONArray listToJsonArray(List<CourseSection> paginationList) {
        JSONArray output = new JSONArray();
        paginationList.forEach(courseSection -> {
            JSONObject sth = new JSONObject();
            String courseSectionInstructorName = repositoryHandler.findUserByInstructor(courseSection.getInstructor().getId()).getName();
            try {
                sth.put("courseSection", courseSection);
                sth.put("instructor", courseSectionInstructorName);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            output.put(sth);
        });
        return output;
    }

    private List<CourseSection> pagination(List<CourseSection> filteredList, Integer pageNumber, Integer maxInEachPage) {
        errorChecker.checkPaginationErrors(filteredList.size(), pageNumber, maxInEachPage);
        if(pageNumber == null && maxInEachPage==null){
            return filteredList;
        }else if(pageNumber != null && maxInEachPage != null){
            return filteredList.subList((pageNumber - 1)  * maxInEachPage, (pageNumber)  * maxInEachPage);
        }else if(pageNumber!= null && maxInEachPage == null){
            return filteredList.subList((pageNumber - 1)  * DEFAULT_MAX_IN_EACH_PAGE, (pageNumber)  * DEFAULT_MAX_IN_EACH_PAGE);
        }else{
            return filteredList.subList(0, maxInEachPage> filteredList.size() ? filteredList.size() : maxInEachPage);
        }
    }

    private List<CourseSection> filterList(Long termId, String instructorName, String courseName) {
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
        errorChecker.checkCourseSectionExists(termId, courseSection);
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


    public String read(Long id, String username, String password){
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private JSONObject read(Long id) {
        CourseSection courseSection = repositoryHandler.findCourseSection(id);
        int numberOfStudents = repositoryHandler.findNumberOfStudentsInCourseSection(courseSection.getId());
        JSONObject output = new JSONObject();
        try{
            output.put("courseSection", courseSection);
            output.put("numberOfStudents",numberOfStudents);
        }catch (JSONException j){
            j.printStackTrace();
        }

        return output;
    }


    public String update(Long termId, Long courseId, Long instructorId, Long courseSectionId, String username, String password) {
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSectionId);
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
    }

    public void delete(Long courseSectionId, String username, String password){
        errorChecker.checkIsInstructorOfCourseSection(username, password, courseSectionId);
        errorChecker.checkCourseSectionIsNotEmpty(courseSectionId);
        CourseSection courseSection = repositoryHandler.findCourseSection(courseSectionId);
        repositoryHandler.deleteCourseSection(courseSection);
    }
}
