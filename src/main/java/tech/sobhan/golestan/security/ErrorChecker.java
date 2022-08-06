package tech.sobhan.golestan.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.sobhan.golestan.business.exceptions.*;
import tech.sobhan.golestan.business.exceptions.duplication.*;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.Repository;

import java.util.List;

import static tech.sobhan.golestan.security.PasswordEncoder.hash;

@Component
public class ErrorChecker {
    private final Repository repository;

    public ErrorChecker(Repository repository) {
        this.repository = repository;
    }

    private boolean isUser(String username, String password){
        User user = repository.getUserByUsername(username);
        return user.getPassword().equals(hash(password));
    }

    private boolean isNotAdmin(String username){
        User user = repository.getUserByUsername(username);
        return !user.isAdmin();
    }

    private boolean isInstructor(String username){
        User user = repository.getUserByUsername(username);
        return user.getInstructor()!=null;
    }

    private boolean isNotInstructorOfCourseSection(String username, CourseSection courseSection) {
        User user = repository.getUserByUsername(username);
        return !user.getInstructor().equals(courseSection.getInstructor());
    }

    public void checkIsUser(String username, String password) {
        if(!isUser(username, password)) throw new UnauthorisedException();
        checkIsActive(username);
    }

    private void checkIsActive(String username) {
        User user = repository.getUserByUsername(username);
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
        CourseSection courseSection = repository.findCourseSection(courseSectionId);
        checkIsInstructorOfCourseSection(username, password, courseSection);
    }
    public void checkIsInstructorOfCourseSection(String username, String password, CourseSection courseSection) {
        checkIsInstructor(username, password);
        if(isNotInstructorOfCourseSection(username, courseSection)) throw new ForbiddenException();
    }

    public void checkCourseExists(Course course) {
        List<Course> allCourses = repository.findAllCourses();
        for (Course c : allCourses) {
            if(course.equals(c)){
                throw new CourseDuplicationException();
            }
        }
    }

    @Value("${DEFAULT_MAX_IN_EACH_PAGE}")
    private static Integer defaultMaxInEachPage;//todo fix two responses when advising exception
    public static  void checkPaginationErrors(int size, Integer pageNumber, Integer maxInEachPage) {
        if(pageNumber==null)
            if(maxInEachPage< 1) throw new PageNumberException();
            else return;
        if(maxInEachPage == null) maxInEachPage = defaultMaxInEachPage;
        if(pageNumber < 1 || maxInEachPage < 1) throw new PageNumberException();
        if(size < (pageNumber) * maxInEachPage) throw new PageNumberException();
    }

    public void checkPhoneNumber(String phone) {
        if(!phone.matches("\\d{11}")) throw new InvalidPhoneNumberException();
    }

    public void checkCourseSectionExists(Term term, CourseSection courseSection) {
        if(repository.courseSectionExistsByTerm(term, courseSection)) throw new CourseSectionDuplicationException();
    }

    public void checkUserExists(String username, String phone, String nationalId) {
        if(repository.userExistsByUsername(username) ||
                repository.userExistsByPhone(phone) ||
                repository.userExistsByNationalId(nationalId)) throw new UserDuplicationException();
    }


    public void checkTermExists(String title) {
        if(repository.termExistsByTitle(title)) throw new TermDuplicationException();
    }

    public void checkCourseSectionRegistrationExists(CourseSection courseSection, Student student) {
        if(repository.csrExistsByCourseSectionAndStudent(courseSection, student))
            throw new CourseSectionRegistrationDuplicationException();
    }

    public void checkCourseSectionIsNotEmpty(CourseSection courseSection) {
        if(!repository.findCourseSectionRegistrationByCourseSection(courseSection).isEmpty())
            throw new CourseSectionRegistrationNotEmptyException();
    }

    public void checkNationalId(String nationalId) {
        if(!nationalId.matches("\\d{10}")) throw new InvalidNationalIdException();
    }
}
