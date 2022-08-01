package tech.sobhan.golestan.services;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.business.exceptions.CourseNotFoundException;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.repositories.CourseRepository;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;
import static tech.sobhan.golestan.utils.Util.deleteLog;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final ErrorChecker errorChecker;

    public CourseService(CourseRepository courseRepository,
                         ErrorChecker errorChecker) {
        this.courseRepository = courseRepository;
        this.errorChecker = errorChecker;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return list().toString();
    }

    private List<Course> list() {
        return courseRepository.findAll();
    }

    public String create(int units, String title, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        Course course = Course.builder().units(units).title(title).build();
        return create(course).toString();
    }

    public Course create(Course course) {
        createLog(Course.class, course.getId());
        return courseRepository.save(course);
    }

    private boolean courseExists(List<Course> allCourses, Course course) {//todo find usage
        for (Course c : allCourses) {
            if(course.equals(c)){
                System.out.println("ERROR403 duplicate Courses");
                return true;
            }
        }
        return false;
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private Course read(Long id) {
        return courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
    }

    public String update(Course newCourse, Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        update(newCourse, id);
        return "OK";
    }

    private void update(Course newCourse, Long id) {
        courseRepository.findById(id).map(user -> {
            user = newCourse.clone();
            return courseRepository.save(user);
        }).orElseGet(() -> {
            newCourse.setId(id);
            return courseRepository.save(newCourse.clone());
        });
    }

    public String delete(Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        courseRepository.delete(read(id));
        deleteLog(Course.class, id);
        return "OK";
    }


}
