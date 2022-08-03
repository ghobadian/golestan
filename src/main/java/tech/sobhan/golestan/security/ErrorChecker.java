package tech.sobhan.golestan.security;

import org.springframework.stereotype.Component;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.business.exceptions.PageNumberException;
import tech.sobhan.golestan.business.exceptions.ForbiddenException;
import tech.sobhan.golestan.business.exceptions.UnauthorisedException;
import tech.sobhan.golestan.business.exceptions.duplication.CourseDuplicationException;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.repositories.RepositoryHandler;

import java.util.List;

@Component
public class ErrorChecker {
    private final RepositoryHandler repositoryHandler;

    public ErrorChecker(RepositoryHandler repositoryHandler) {
        this.repositoryHandler = repositoryHandler;
    }

    private boolean isUser(String username, String password){
        User user = repositoryHandler.getUserByUsername(username);
//        PasswordEncoder passwordEncoder = getPasswordEncoder();
        return user.getPassword().equals(password);
    }

    private boolean isNotAdmin(String username){
        User user = repositoryHandler.getUserByUsername(username);
        return !user.isAdmin();
    }

    private boolean isInstructor(String username){
        User user = repositoryHandler.getUserByUsername(username);
        return user.getInstructor()!=null;
    }

    private boolean isNotInstructorOfCourseSection(String username, CourseSection courseSection) {
        User user = repositoryHandler.getUserByUsername(username);
        return !user.getInstructor().equals(courseSection.getInstructor());
    }

    public void checkIsAdmin(String username, String password) {
        checkIsUser(username, password);
        if(isNotAdmin(username)) throw new ForbiddenException();
    }

    public void checkIsUser(String username, String password) {
        if(!isUser(username, password)) throw new UnauthorisedException();
    }

    public void checkIsInstructor(String username, String password) {
        checkIsUser(username, password);
        if(!isInstructor(username)) throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSectionOrAdmin(String username, String password, CourseSection courseSection){//todo clean it
        checkIsUser(username, password);
        if(isInstructor(username) && isNotAdmin(username)) throw new ForbiddenException();
        if(isInstructor(username) && isNotInstructorOfCourseSection(username, courseSection))
            throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSection(String username, String password, CourseSection courseSection) {
        checkIsInstructor(username, password);
        if(isNotInstructorOfCourseSection(username, courseSection)) throw new ForbiddenException();
    }

    public void checkCourseExists(Course course) {
        List<Course> allCourses = repositoryHandler.findAllCourses();
        for (Course c : allCourses) {
            if(course.equals(c)){
                throw new CourseDuplicationException();
            }
        }
    }

    public void checkPageLength(int size, Integer pageNumber, Integer maxInEachPage) {
        if(size < (pageNumber-1) * maxInEachPage) throw new PageNumberException();
    }
}
