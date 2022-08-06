package tech.sobhan.golestan.security;

import org.springframework.stereotype.Component;
import tech.sobhan.golestan.business.exceptions.*;
import tech.sobhan.golestan.business.exceptions.duplication.*;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.RepositoryHandler;

import java.util.List;

import static tech.sobhan.golestan.constants.Etc.DEFAULT_MAX_IN_EACH_PAGE;
import static tech.sobhan.golestan.security.PasswordEncoder.hash;

@Component
public class ErrorChecker {
    private final RepositoryHandler repositoryHandler;

    public ErrorChecker(RepositoryHandler repositoryHandler) {
        this.repositoryHandler = repositoryHandler;
    }

    private boolean isUser(String username, String password){
        User user = repositoryHandler.getUserByUsername(username);
        return user.getPassword().equals(hash(password));
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

    public void checkIsUser(String username, String password) {
        if(!isUser(username, password)) throw new UnauthorisedException();
        checkIsActive(username);
    }

    private void checkIsActive(String username) {
        User user = repositoryHandler.getUserByUsername(username);
        if(!user.isActive()) throw new UserNotActiveException();
    }

    public void checkIsAdmin(String username, String password) {
        checkIsUser(username, password);
        if(isNotAdmin(username)) throw new ForbiddenException();
    }

    public void checkIsInstructor(String username, String password) {
        checkIsUser(username, password);
        if(!isInstructor(username)) throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSectionOrAdmin(String username, String password, CourseSection courseSection){
        checkIsUser(username, password);
        if(isInstructor(username) && isNotAdmin(username)) throw new ForbiddenException();
        if(isInstructor(username) && isNotInstructorOfCourseSection(username, courseSection)) throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSection(String username, String password, Long courseSectionId) {
        CourseSection courseSection = repositoryHandler.findCourseSection(courseSectionId);
        checkIsInstructorOfCourseSection(username, password, courseSection);
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

    public void checkPaginationErrors(int size, Integer pageNumber, Integer maxInEachPage) {
        if(pageNumber==null)
            if(maxInEachPage< 1) throw new PageNumberException();
            else return;
        if(maxInEachPage == null) maxInEachPage = DEFAULT_MAX_IN_EACH_PAGE;
        if(pageNumber < 1 || maxInEachPage < 1) throw new PageNumberException();
        if(size < (pageNumber) * maxInEachPage) throw new PageNumberException();
    }

    public void checkPhoneNumber(String phone) {
        if(!phone.matches("\\d{11}")) throw new InvalidPhoneNumberException();
    }

    public void checkCourseSectionExists(Long termId, CourseSection courseSection) {
        if(repositoryHandler.courseSectionExistsByTerm(termId, courseSection)) throw new CourseSectionDuplicationException();
    }

    public void checkUserExists(String username, String phone, String nationalId) {
        if(repositoryHandler.userExistsByUsername(username) ||
                repositoryHandler.userExistsByPhone(phone) ||
                repositoryHandler.userExistsByNationalId(nationalId)) throw new UserDuplicationException();
    }


    public void checkTermExists(String title) {
        if(repositoryHandler.termExistsByTitle(title)) throw new TermDuplicationException();
    }

    public void checkCourseSectionRegistrationExists(Long courseSectionId, Long studentId) {
        if(repositoryHandler.courseSectionRegistrationExistsByCourseSectionAndStudent(courseSectionId, studentId))
            throw new CourseSectionRegistrationDuplicationException();
    }

    public void checkCourseSectionIsNotEmpty(Long courseSectionId) {
        if(!repositoryHandler.findCourseSectionRegistrationByCourseSection(courseSectionId).isEmpty())
            throw new CourseSectionRegistrationNotEmptyException();
    }

    public void checkNationalId(String nationalId) {
        if(!nationalId.matches("\\d{10}")) throw new InvalidNationalIdException();
    }
}
