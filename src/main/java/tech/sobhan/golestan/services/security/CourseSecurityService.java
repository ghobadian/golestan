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

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return service.list().toString();
    }

    public String create(int units, String title, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        Course course = Course.builder().units(units).title(title).build();
        errorChecker.checkCourseExists(course);
        return service.create(course).toString();
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return service.read(id).toString();
    }

    public String update(Long courseId, String username, String password, String title, Integer units) {
        errorChecker.checkIsAdmin(username, password);
        return service.update(courseId, title, units).toString();
    }

    public void delete(Long courseId, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        service.delete(courseId);
    }
}
