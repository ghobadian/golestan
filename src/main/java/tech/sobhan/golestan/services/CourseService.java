package tech.sobhan.golestan.services;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.repositories.Repository;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;
import static tech.sobhan.golestan.utils.Util.deleteLog;

@Service
public class CourseService {
    private final Repository repository;
    private final ErrorChecker errorChecker;

    public CourseService(Repository repository,
                         ErrorChecker errorChecker) {
        this.repository = repository;
        this.errorChecker = errorChecker;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return list().toString();
    }

    public List<Course> list() {
        return repository.findAllCourses();
    }

    public String create(int units, String title, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        Course course = Course.builder().units(units).title(title).build();
        errorChecker.checkCourseExists(course);
        return create(course).toString();
    }

    public Course create(Course course) {
        createLog(Course.class, course.getId());
        return repository.saveCourse(course);
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private Course read(Long id) {
        return repository.findCourse(id);
    }

    public String update(Long courseId, String username, String password, String title, Integer units) {
        errorChecker.checkIsAdmin(username, password);
        return update(courseId, title, units).toString();
    }

    private Course update(Long courseId, String title, Integer units) {
        Course course = repository.findCourse(courseId);
        updateTitle(title, course);
        updateUnits(units, course);
        return repository.saveCourse(course);
    }

    private void updateUnits(Integer units, Course course) {
        if(units !=null){
            course.setUnits(units);
        }
    }

    private void updateTitle(String title, Course course) {
        if(title !=null){
            course.setTitle(title);
        }
    }

    public void delete(Long courseId, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        Course course = repository.findCourse(courseId);
        repository.deleteCourse(course);
        deleteLog(Course.class, courseId);
    }
}
