package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.enums.Degree;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.enums.Role;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.RepositoryHandler;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.Date;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
public class AdminService {
    private final RepositoryHandler repositoryHandler;
    private final ErrorChecker errorChecker;
    private final UserService userService;

    public AdminService(RepositoryHandler repositoryHandler, ErrorChecker errorChecker, UserService userService) {
        this.repositoryHandler = repositoryHandler;
        this.errorChecker = errorChecker;
        this.userService = userService;
    }

    public String modifyRole(Long id, Map<String, String> requestedBody, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        User foundUser = repositoryHandler.findUser(id);
        foundUser.setActive(true);
        Role role = Role.valueOf(requestedBody.get("role").toUpperCase());
        switch (role){
            case STUDENT : addRoleStudent(requestedBody, foundUser); break;
            case INSTRUCTOR : addRoleInstructor(requestedBody, foundUser); break;
        }
        return "OK";
    }


    private void addRoleInstructor(Map<String, String> requestedBody, User user) {
        Instructor instructor = Instructor.builder().rank(Rank.valueOf(requestedBody.get("rank").toUpperCase())).build();
        repositoryHandler.saveInstructor(instructor);
        user.setInstructor(instructor);
        repositoryHandler.saveUser(user);
    }

    private void addRoleStudent(Map<String, String> requestedBody, User user) {
        Degree degree = Degree.valueOf(Optional.of(requestedBody.get("degree")).orElseThrow().toUpperCase());
        Student student = Student.builder().degree(degree)
                .startDate(new Date()).build();
        user.setStudent(student);
        repositoryHandler.saveStudent(student);
        repositoryHandler.saveUser(user);
    }
}
