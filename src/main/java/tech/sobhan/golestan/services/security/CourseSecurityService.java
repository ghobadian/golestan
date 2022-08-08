package tech.sobhan.golestan.services.security;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.security.ErrorChecker;
import tech.sobhan.golestan.services.CourseService;

@Service
public class CourseSecurityService {
    private final ErrorChecker errorChecker;
    private final CourseService service;

    public CourseSecurityService(ErrorChecker errorChecker, CourseService service) {
        this.errorChecker = errorChecker;
        this.service = service;
    }

    public String list(String token) {
        errorChecker.checkIsUser(token);
        return service.list().toString();
    }

    public String create(int units, String title, String token) {
        errorChecker.checkIsAdmin(token);
        errorChecker.checkCourseExistsByTitle(title);
        Course course = Course.builder().units(units).title(title).build();
        return service.create(course).toString();
    }

    public String read(Long id, String token) {
        errorChecker.checkIsUser(token);
        return service.read(id).toString();
    }

    public String update(Long courseId, String token, String title, Integer units) {
        errorChecker.checkIsAdmin(token);
        return service.update(courseId, title, units).toString();
    }

    public void delete(Long courseId, String token) {
        errorChecker.checkIsAdmin(token);
        service.delete(courseId);
    }
}
