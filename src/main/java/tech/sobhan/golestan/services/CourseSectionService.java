package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import tech.sobhan.golestan.repositories.Repository;

import java.util.List;

import static tech.sobhan.golestan.security.ErrorChecker.checkPaginationErrors;
import static tech.sobhan.golestan.utils.Util.createLog;

@Service
@RequiredArgsConstructor
public class CourseSectionService {
    private final Repository repository;

    public String list(Long termId, String instructorName,
                       String courseName, Integer pageNumber, Integer maxInEachPage) {
        Term term = repository.findTerm(termId);
        List<CourseSection> filteredList = filterList(term, instructorName, courseName);
        checkPaginationErrors(filteredList.size(), pageNumber, maxInEachPage);
        List<CourseSection> paginationList = pagination(filteredList, pageNumber, maxInEachPage);
        JSONArray output = listToJsonArray(paginationList);
        return output.toString();
    }

    private JSONArray listToJsonArray(List<CourseSection> paginationList) {
        JSONArray output = new JSONArray();
        paginationList.forEach(courseSection -> {
            JSONObject sth = new JSONObject();
            String courseSectionInstructorName = repository.findUserByInstructor(courseSection.getInstructor().getId()).getName();
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

    @Value("${DEFAULT_MAX_IN_EACH_PAGE}")
    private static Integer defaultMaxInEachPage;
    private List<CourseSection> pagination(List<CourseSection> filteredList, Integer pageNumber, Integer maxInEachPage) {
        if(pageNumber == null && maxInEachPage==null){
            return filteredList;
        }else if(pageNumber != null && maxInEachPage != null){
            return filteredList.subList((pageNumber - 1)  * maxInEachPage, (pageNumber)  * maxInEachPage);
        }else if(pageNumber!= null && maxInEachPage == null){
            return filteredList.subList((pageNumber - 1)  * defaultMaxInEachPage, (pageNumber)  * defaultMaxInEachPage);
        }else{
            return filteredList.subList(0, maxInEachPage> filteredList.size() ? filteredList.size() : maxInEachPage);
        }
    }

    private List<CourseSection> filterList(Term term, String instructorName, String courseName) {
        if(instructorName!=null && courseName == null){
            return repository.findCourseSectionByInstructorName(instructorName);
        }else if(instructorName==null && courseName!= null){
            return repository.findCourseSectionByCourseName(courseName);
        }else if(instructorName!=null && courseName!= null){
            return repository.findCourseSectionByInstructorNameAndCourseName(instructorName, courseName);
        }else{
            return list(term);
        }
    }

    private List<CourseSection> list(Term term) {
        return repository.findCourseSectionByTerm(term);
    }

    public CourseSection create(CourseSection courseSection) {
        createLog(CourseSection.class, courseSection.getId());
        return repository.saveCourseSection(courseSection);
    }

    public JSONObject read(Long id) {
        CourseSection courseSection = repository.findCourseSection(id);
        int numberOfStudents = repository.findNumberOfStudentsInCourseSection(courseSection);
        JSONObject output = new JSONObject();
        try{
            output.put("courseSection", courseSection);
            output.put("numberOfStudents",numberOfStudents);
        }catch (JSONException j){
            j.printStackTrace();
        }
        return output;
    }

    public String update(Long termId, Long courseId, Long instructorId, Long courseSectionId) {
        CourseSection courseSection = repository.findCourseSection(courseSectionId);
        try{
            Term term = repository.findTerm(termId);
            courseSection.setTerm(term);
        }catch (TermNotFoundException ignored){
        }
        try{
            Course course = repository.findCourse(courseId);
            courseSection.setCourse(course);
        }catch (CourseNotFoundException ignored){
        }
        try{
            Instructor instructor = repository.findInstructor(instructorId);
            courseSection.setInstructor(instructor);
        }catch (InstructorNotFoundException ignored){
        }
        repository.saveCourseSection(courseSection);
        return "OK";
    }

    public void delete(CourseSection courseSection){
        repository.deleteCourseSection(courseSection);
    }
}
