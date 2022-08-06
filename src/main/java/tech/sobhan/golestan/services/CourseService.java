package tech.sobhan.golestan.services;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.repositories.Repository;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;
import static tech.sobhan.golestan.utils.Util.deleteLog;

@Service
public class CourseService {
    private final Repository repository;

    public CourseService(Repository repository) {
        this.repository = repository;
    }

    public List<Course> list() {
        return repository.findAllCourses();
    }

    public Course create(Course course) {
        createLog(Course.class, course.getId());
        return repository.saveCourse(course);
    }

    public Course read(Long id) {
        return repository.findCourse(id);
    }

    public Course update(Long courseId, String title, Integer units) {
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

    public void delete(Long courseId) {
        Course course = repository.findCourse(courseId);
        repository.deleteCourse(course);
        deleteLog(Course.class, courseId);
    }
}
