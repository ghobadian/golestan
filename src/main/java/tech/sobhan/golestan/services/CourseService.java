package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.dao.Repo;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;
import static tech.sobhan.golestan.utils.Util.deleteLog;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final Repo repo;

    public List<Course> list() {
        return repo.findAllCourses();
    }

    public Course create(Course course) {
        createLog(Course.class, course.getId());
        return repo.saveCourse(course);
    }

    public Course read(Long id) {
        return repo.findCourse(id);
    }

    public Course update(Long courseId, String title, Integer units) {
        Course course = repo.findCourse(courseId);
        updateTitle(title, course);
        updateUnits(units, course);
        return repo.saveCourse(course);
    }

    private void updateUnits(Integer units, Course course) {
        if(units !=null) {
            course.setUnits(units);
        }
    }

    private void updateTitle(String title, Course course) {
        if(title !=null) {
            course.setTitle(title);
        }
    }

    public void delete(Long courseId) {
        Course course = repo.findCourse(courseId);
        repo.deleteCourse(course);
        deleteLog(Course.class, courseId);
    }
}
