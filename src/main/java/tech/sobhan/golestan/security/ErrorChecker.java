package tech.sobhan.golestan.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.sobhan.golestan.business.exceptions.*;
import tech.sobhan.golestan.business.exceptions.duplication.*;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.Repository;

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

    private boolean isUser(String token){
        return repository.userExistsByToken(token);
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

    public void checkIsUser(String token) {
        if(!isUser(token)) throw new UnauthorisedException();
    }

    private void checkIsActive(String username) {
        User user = repository.getUserByUsername(username);
        if(!user.isActive()) throw new UserNotActiveException();
    }

    public void checkIsAdmin(String token) {
        checkIsUser(token);
        String username = repository.findTokenByToken(token).getUsername();
        if(isNotAdmin(username)) throw new ForbiddenException();
    }

    public void checkIsInstructor(String token) {
        checkIsUser(token);
        String username = repository.findTokenByToken(token).getUsername();
        if(!isInstructor(username)) throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSectionOrAdmin(String token, CourseSection courseSection){
        checkIsUser(token);
        String username = repository.findTokenByToken(token).getUsername();
        if(isInstructor(username) && isNotAdmin(username)) throw new ForbiddenException();
        if(isInstructor(username) && isNotInstructorOfCourseSection(username, courseSection)) throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSection(String token, Long courseSectionId) {
        CourseSection courseSection = repository.findCourseSection(courseSectionId);
        checkIsInstructorOfCourseSection(token, courseSection);
    }
    public void checkIsInstructorOfCourseSection(String token, CourseSection courseSection) {
        checkIsInstructor(token);
        String username = repository.findTokenByToken(token).getUsername();
        if(isNotInstructorOfCourseSection(username, courseSection)) throw new ForbiddenException();
    }

    public void checkCourseExistsByTitle(String title) {
        if(repository.courseExistsByTitle(title)) throw new CourseDuplicationException();
    }

    @Value("${defaultMaxInEachPage}")
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


    public void checkTokenExistsByUsername(String username) {
        if(repository.tokenExistsByUsername(username)) throw new TokenDuplicationException();
    }
}
