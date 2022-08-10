package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.enums.Degree;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.enums.Role;
import tech.sobhan.golestan.models.Token;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.security.PasswordEncoder;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final Repo repo;
    private final InstructorService instructorService;
    private final StudentService studentService;

    private final PasswordEncoder passwordEncoder;

    public List<User> list() {
        return repo.findAllUsers();
    }

    public User create(String username, String password, String name, String phone, String nationalId) {
        User user = User.builder().username(username).password(passwordEncoder.hash(password))
                .name(name).phone(phone).nationalId(nationalId)
                .active(false).admin(false).build();
        log.info("User with username " + username + " created");
        return repo.saveUser(user);
    }

    public User read(Long id) {
        return repo.findUser(id);
    }

    public User update(String name, String newUsername, String newPassword, String phone, String username) {
        User user = repo.findUserByUsername(username);
        updateName(name, user);
        updateUsername(newUsername, username, user);
        updatePassword(newPassword, user);
        updatePhoneNumber(phone, user);
        return repo.saveUser(user);
    }

    private void updatePhoneNumber(String phone, User user) {
        if(phone !=null && !repo.userExistsByPhone(phone)) {
            user.setPhone(phone);
        }
    }

    private void updatePassword(String newPassword, User user) {
        if(newPassword !=null) {
            user.setPassword(passwordEncoder.hash(newPassword));
        }
    }

    private void updateUsername(String newUsername, String username, User user) {
        if(newUsername !=null && !repo.userExistsByUsername(username)) {
            user.setUsername(newUsername);
        }
    }

    private void updateName(String name, User user) {
        if(name !=null) {
            user.setName(name);
        }
    }

    public void delete(Long id) {
        User user = repo.findUser(id);
        deleteInstructorOfUser(user);
        deleteStudentOfUser(user);
        repo.deleteUser(user);
        log.info("User with id" + id + "deleted");
    }

    private void deleteStudentOfUser(User user) {
        if(user.getInstructor() != null) {
            Long instructorId = user.getInstructor().getId();
            user.setInstructor(null);
            instructorService.delete(instructorId);
        }
    }

    private void deleteInstructorOfUser(User user) {
        if(user.getStudent() != null) {
            Long studentId = user.getStudent().getId();
            user.setStudent(null);
            studentService.delete(studentId);
        }
    }

    public User modifyRole(Long id, Map<String, String> requestedBody) {
        User foundUser = repo.findUser(id);
        foundUser.setActive(true);
        Role role = Role.valueOf(requestedBody.get("role").toUpperCase());
        switch (role) {
            case STUDENT : return addRoleStudent(requestedBody, foundUser);
            case INSTRUCTOR : return addRoleInstructor(requestedBody, foundUser);
        }
        throw new RuntimeException();
    }


    private User addRoleInstructor(Map<String, String> requestedBody, User user) {
        Instructor instructor = Instructor.builder().rank(Rank.valueOf(requestedBody.get("rank").toUpperCase())).build();
        repo.saveInstructor(instructor);
        user.setInstructor(instructor);
        return repo.saveUser(user);
    }

    private User addRoleStudent(Map<String, String> requestedBody, User user) {
        Degree degree = Degree.valueOf(Optional.of(requestedBody.get("degree")).orElseThrow().toUpperCase());
        Student student = Student.builder().degree(degree)
                .startDate(new Date()).build();
        user.setStudent(student);
        repo.saveStudent(student);
        return repo.saveUser(user);
    }

    public String saveAndSendToken(String username) {
        String tokenContent = UUID.randomUUID().toString();
        Token token = Token.builder().username(username).token(tokenContent).build();
        return repo.saveToken(token).toString();
    }

    public void logout(Token token) {
        repo.deleteToken(token);
    }
}
