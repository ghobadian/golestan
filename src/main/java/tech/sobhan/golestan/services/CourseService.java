package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.Course;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {
    private final Repo repo;

    public List<Course> list() {
        return repo.findAllCourses();
    }

    public List<Course> list(int page, int number) {
        return repo.findAllCourses(page, number);
    }

    public Course create(int units, String title) {
        Course course = Course.builder().units(units).title(title).build();
        log.info("Course " + course.getTitle() + " created");
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
        log.info("Course " + course.getTitle() + " deleted");
    }
}
