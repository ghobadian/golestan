package tech.sobhan.golestan.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.CourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSecurityService {
    private final ErrorChecker errorChecker;
    private final CourseService service;

    public List<Course> list(String token) {
        errorChecker.checkIsUser(token);
        return service.list();
    }

    public Course create(int units, String title, String token) {
        errorChecker.checkIsAdmin(token);
        errorChecker.checkCourseExistsByTitle(title);
        Course course = Course.builder().units(units).title(title).build();
        return service.create(course);
    }

    public Course read(Long id, String token) {
        errorChecker.checkIsUser(token);
        return service.read(id);
    }

    public Course update(Long courseId, String token, String title, Integer units) {
        errorChecker.checkIsAdmin(token);
        return service.update(courseId, title, units);
    }

    public void delete(Long courseId, String token) {
        errorChecker.checkIsAdmin(token);
        service.delete(courseId);
    }
}
