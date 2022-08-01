package tech.sobhan.golestan.security;

import org.springframework.stereotype.Component;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.business.exceptions.ForbiddenException;
import tech.sobhan.golestan.business.exceptions.UnauthorisedException;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.repositories.RepositoryHandler;

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

    private boolean isAdmin(String username){
        User user = repositoryHandler.getUserByUsername(username);
        return user.isAdmin();
    }

    private boolean isInstructor(String username){
        User user = repositoryHandler.getUserByUsername(username);
        return user.getInstructor()!=null;
    }

    private boolean isInstructorOfCourseSection(String username, CourseSection courseSection) {
        User user = repositoryHandler.getUserByUsername(username);
        return user.getInstructor().equals(courseSection.getInstructor());
    }

    public void checkIsAdmin(String username, String password) {
        checkIsUser(username, password);
        if(!isAdmin(username)) throw new ForbiddenException();
    }

    public void checkIsUser(String username, String password) {
        if(!isUser(username, password)) throw new UnauthorisedException();
    }

    public void checkIsInstructor(String username, String password) {
        checkIsUser(username, password);
        if(!isInstructor(username)) throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSection(String username, String password, CourseSection courseSection) {
        checkIsInstructor(username, password);
        if(!isInstructorOfCourseSection(username, courseSection)) throw new ForbiddenException();
    }
}
