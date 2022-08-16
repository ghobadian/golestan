package tech.sobhan.golestan.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.sobhan.golestan.business.exceptions.*;
import tech.sobhan.golestan.business.exceptions.duplication.*;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.User;

@Component
@RequiredArgsConstructor
public class ErrorChecker {
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
        if (!isUser(username, password)) throw new UnauthorisedException();
        checkIsActive(username);
    }

    public void checkIsUser(String token) {
        if (!isUser(token)) throw new UnauthorisedException();
    }

    private void checkIsActive(String username) {
        User user = repo.getUserByUsername(username);
        if (!user.isActive()) throw new UserNotActiveException();
    }

    public void checkIsAdmin(String token) {
        checkIsUser(token);
        String username = repo.findUsernameByToken(token);
        if (isNotAdmin(username)) throw new ForbiddenException();
    }

    public void checkIsInstructor(String token) {
        checkIsUser(token);
        String username = repo.findUsernameByToken(token);
        if (!isInstructor(username)) throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSectionOrAdmin(String token, CourseSection courseSection) {
        checkIsUser(token);
        String username = repo.findUsernameByToken(token);
        if (isInstructor(username) && isNotAdmin(username)) throw new ForbiddenException();
        if (isInstructor(username) && isNotInstructorOfCourseSection(username, courseSection)) throw new ForbiddenException();
    }

    public void checkIsInstructorOfCourseSectionOrAdmin(String token, Long courseSectionId) {
        CourseSection cs = repo.findCourseSection(courseSectionId);
        checkIsInstructorOfCourseSectionOrAdmin(token, cs);
    }

    public void checkIsInstructorOfCourseSection(String token, Long courseId, Long instructorId, Long termId) {
        CourseSection courseSection = CourseSection.builder().course(repo.findCourse(courseId))
                .instructor(repo.findInstructor(instructorId))
                .term(repo.findTerm(termId)).build();
        checkIsInstructorOfCourseSection(token, courseSection);
    }

    public void checkIsInstructorOfCourseSection(String token, Long courseSectionId) {
        CourseSection courseSection = repo.findCourseSection(courseSectionId);
        checkIsInstructorOfCourseSection(token, courseSection);
    }

    public void checkIsInstructorOfCourseSection(String token, CourseSection courseSection) {
        checkIsInstructor(token);
        String username = repo.findUsernameByToken(token);
        if (isNotInstructorOfCourseSection(username, courseSection)) throw new ForbiddenException();
    }

    public void checkCourseExistsByTitle(String title) {
        if (repo.courseExistsByTitle(title)) throw new CourseDuplicationException();
    }

    public void checkPhoneNumber(String phone) {
        if (!phone.matches("\\d{11}")) throw new InvalidPhoneNumberException();
    }

    public void checkCourseSectionExists(Long courseId, Long instructorId, Long termId) {
        CourseSection courseSection = CourseSection.builder().course(repo.findCourse(courseId))
                .instructor(repo.findInstructor(instructorId)).term(repo.findTerm(termId)).build();
        Term term = repo.findTerm(termId);
        checkCourseSectionExists(term, courseSection);
    }

    public void checkCourseSectionExists(Term term, CourseSection courseSection) {
        if (repo.courseSectionExistsByTerm(term, courseSection)) throw new CourseSectionDuplicationException();
    }

    public void checkUserExists(String username, String phone, String nationalId) {
        if (repo.userExistsByUsername(username) ||
                repo.userExistsByPhone(phone) ||
                repo.userExistsByNationalId(nationalId)) throw new UserDuplicationException();
    }

    public void checkTermExists(String title) {
        if (repo.termExistsByTitle(title)) throw new TermDuplicationException();
    }

    public void checkCourseSectionRegistrationExists(Long courseSectionId, Long studentId) {
        if (repo.csrExistsByCourseSectionIdAndStudentId(courseSectionId, studentId))
            throw new CourseSectionRegistrationDuplicationException();

    }

    public void checkCourseSectionRegistrationExists(Long courseSectionId, String token) {
        String studentUsername = repo.findUsernameByToken(token);
        Long studentId = repo.findStudentByUsername(studentUsername).getId();
        checkCourseSectionRegistrationExists(courseSectionId, studentId);
    }

    public void checkCourseSectionIsNotEmpty(Long courseSectionId) {
        if (!repo.findCourseSectionRegistrationByCourseSectionId(courseSectionId).isEmpty())
            throw new CourseSectionRegistrationNotEmptyException();
    }

    public void checkNationalId(String nationalId) {
        if (!nationalId.matches("\\d{10}")) throw new InvalidNationalIdException();
    }

    public void checkTokenExistsByUsername(String username) {
        if (repo.tokenExistsByUsername(username)) throw new TokenDuplicationException();
    }
}
