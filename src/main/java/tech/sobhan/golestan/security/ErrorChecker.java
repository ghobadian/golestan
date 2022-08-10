package tech.sobhan.golestan.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.sobhan.golestan.business.exceptions.*;
import tech.sobhan.golestan.business.exceptions.duplication.*;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;

@Component
@RequiredArgsConstructor
public class ErrorChecker {
    private static PaginationErrorChecker paginationErrorChecker;
    private final Repo repo;
    private final PasswordEncoder passwordEncoder;

    private boolean isUser(String username, String password) {
        User user = repo.getUserByUsername(username);
        return user.getPassword().equals(passwordEncoder.hash(password));
    }

    private boolean isUser(String token) {
        return repo.userExistsByToken(token);
    }

    private boolean isNotAdmin(String username) {
        User user = repo.getUserByUsername(username);
        return !user.isAdmin();
    }

    private boolean isInstructor(String username) {
        User user = repo.getUserByUsername(username);
        return user.getInstructor()!=null;
    }

    private boolean isNotInstructorOfCourseSection(String username, CourseSection courseSection) {
        User user = repo.getUserByUsername(username);
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
        User user = repo.getUserByUsername(username);
        if(!user.isActive()) throw new UserNotActiveException();
    }

    public void checkIsAdmin(String token) {
        checkIsUser(token);
        String username = repo.findTokenByToken(token).getUsername();
        if(isNotAdmin(username)) throw new ForbiddenException();
    }

    public void checkIsInstructor(String token) {
        checkIsUser(token);
        String username = repo.findTokenByToken(token).getUsername();
        if(!isInstructor(username)) throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSectionOrAdmin(String token, CourseSection courseSection) {
        checkIsUser(token);
        String username = repo.findTokenByToken(token).getUsername();
        if(isInstructor(username) && isNotAdmin(username)) throw new ForbiddenException();
        if(isInstructor(username) && isNotInstructorOfCourseSection(username, courseSection)) throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSection(String token, Long courseSectionId) {
        CourseSection courseSection = repo.findCourseSection(courseSectionId);
        checkIsInstructorOfCourseSection(token, courseSection);
    }
    public void checkIsInstructorOfCourseSection(String token, CourseSection courseSection) {
        checkIsInstructor(token);
        String username = repo.findTokenByToken(token).getUsername();
        if(isNotInstructorOfCourseSection(username, courseSection)) throw new ForbiddenException();
    }

    public void checkCourseExistsByTitle(String title) {
        if(repo.courseExistsByTitle(title)) throw new CourseDuplicationException();
    }

    public void checkPhoneNumber(String phone) {
        if(!phone.matches("\\d{11}")) throw new InvalidPhoneNumberException();
    }

    public void checkCourseSectionExists(Term term, CourseSection courseSection) {
        if(repo.courseSectionExistsByTerm(term, courseSection)) throw new CourseSectionDuplicationException();
    }

    public void checkUserExists(String username, String phone, String nationalId) {
        if(repo.userExistsByUsername(username) ||
                repo.userExistsByPhone(phone) ||
                repo.userExistsByNationalId(nationalId)) throw new UserDuplicationException();
    }


    public void checkTermExists(String title) {
        if(repo.termExistsByTitle(title)) throw new TermDuplicationException();
    }

    public void checkCourseSectionRegistrationExists(CourseSection courseSection, Student student) {
        if(repo.csrExistsByCourseSectionAndStudent(courseSection, student))
            throw new CourseSectionRegistrationDuplicationException();
    }

    public void checkCourseSectionIsNotEmpty(CourseSection courseSection) {
        if(!repo.findCourseSectionRegistrationByCourseSection(courseSection).isEmpty())
            throw new CourseSectionRegistrationNotEmptyException();
    }

    public void checkNationalId(String nationalId) {
        if(!nationalId.matches("\\d{10}")) throw new InvalidNationalIdException();
    }


    public void checkTokenExistsByUsername(String username) {
        if(repo.tokenExistsByUsername(username)) throw new TokenDuplicationException();
    }
}
