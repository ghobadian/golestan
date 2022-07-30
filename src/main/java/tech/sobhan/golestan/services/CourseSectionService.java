package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.CourseSectionNotFoundException;
import tech.sobhan.golestan.business.exceptions.CourseSectionRegistrationNotEmpty;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.repositories.CourseSectionRegistrationRepository;
import tech.sobhan.golestan.repositories.CourseSectionRepository;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;

@Service
@Slf4j
public class CourseSectionService {
    private final CourseSectionRepository courseSectionRepository;
    private final CourseSectionRegistrationRepository courseSectionRegistrationRepository;

    public CourseSectionService(CourseSectionRepository courseSectionRepository,
                                   CourseSectionRegistrationRepository courseSectionRegistrationRepository) {
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionRegistrationRepository = courseSectionRegistrationRepository;
    }

    public List<CourseSection> list() {
        List<CourseSection> allCourseSections = courseSectionRepository.findAll();
//        if(allCourseSections.isEmpty()){
//            throw new CourseSectionNotFoundException();
//        }
        return allCourseSections;
    }

    public List<CourseSection> list(Long termId) {
        List<CourseSection> allCourseSections = courseSectionRepository.findByTerm(termId);
        if(allCourseSections.isEmpty()){
            throw new CourseSectionNotFoundException();
        }
        return allCourseSections;
    }

    public CourseSection create(Course course, Instructor instructor, Term term) {
        CourseSection courseSection = CourseSection.builder().instructor(instructor).course(course).term(term).build();
        return create(courseSection);
    }

    public CourseSection create(CourseSection courseSection) {
        if(courseSectionExists(list(), courseSection)) return null;
        createLog(CourseSection.class, courseSection.getId());
        return courseSectionRepository.save(courseSection);
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

    public JSONObject read(Long id) throws JSONException {
        CourseSection courseSection = courseSectionRepository.findById(id)
                .orElseThrow(CourseSectionNotFoundException::new);
        int numberOfStudents = courseSectionRegistrationRepository.findByCourseSection(courseSection.getId()).size();
        JSONObject output = new JSONObject();
        output.put("courseSection", courseSection);
        output.put("numberOfStudents",numberOfStudents);
        return output;
    }

    public void update(CourseSection newCourseSection, Long id) {
        courseSectionRepository.findById(id).map(user -> {
            user = newCourseSection.clone();
            return courseSectionRepository.save(user);//todo sus for saveUser instead of user
        }).orElseGet(() -> {
            newCourseSection.setId(id);
            return courseSectionRepository.save(newCourseSection.clone());
        });
    }

    public void delete(Long id) throws JSONException, CourseSectionRegistrationNotEmpty {
        CourseSection specifiedCourseSection = (CourseSection) read(id).get("courseSection");
        if(courseSectionRegistrationRepository.findByCourseSection(id).isEmpty()){
            courseSectionRepository.delete(specifiedCourseSection);
        }else{
            throw new CourseSectionRegistrationNotEmpty();
        }
    }

}
