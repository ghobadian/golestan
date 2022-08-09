package tech.sobhan.golestan.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.enums.Degree;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.enums.Role;
import tech.sobhan.golestan.models.Token;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static tech.sobhan.golestan.security.PasswordEncoder.hash;
import static tech.sobhan.golestan.utils.Util.deleteLog;
import static tech.sobhan.golestan.utils.Util.generateToken;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Repository repository;
    private final InstructorService instructorService;
    private final StudentService studentService;

    public List<User> list() {
        return repository.findAllUsers();
    }

    public User create(String username, String password, String name, String phone, String nationalId) {
        User user = User.builder().username(username).password(hash(password))
                .name(name).phone(phone).nationalId(nationalId)
                .active(false).admin(false).build();
        return repository.saveUser(user);
    }

    public User read(Long id) {
        return repository.findUser(id);
    }

    public User update(String name, String newUsername, String newPassword, String phone, String username) {
        User user = repository.findUserByUsername(username);
        updateName(name, user);
        updateUsername(newUsername, username, user);
        updatePassword(newPassword, user);
        updatePhoneNumber(phone, user);
        return repository.saveUser(user);
    }

    private void updatePhoneNumber(String phone, User user) {
        if(phone !=null && !repository.userExistsByPhone(phone)){
            user.setPhone(phone);
        }
    }

    private void updatePassword(String newPassword, User user) {
        if(newPassword !=null){
            user.setPassword(hash(newPassword));
        }
    }

    private void updateUsername(String newUsername, String username, User user) {
        if(newUsername !=null && !repository.userExistsByUsername(username)){
            user.setUsername(newUsername);
        }
    }

    private void updateName(String name, User user) {
        if(name !=null){
            user.setName(name);
        }
    }

    public void delete(Long id) {
        User user = repository.findUser(id);
        deleteInstructorOfUser(user);
        deleteStudentOfUser(user);
        repository.deleteUser(user);
        deleteLog(User.class, id);
    }

    private void deleteStudentOfUser(User user) {
        if(user.getInstructor() != null){
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

    public String modifyRole(Long id, Map<String, String> requestedBody) {
        User foundUser = repository.findUser(id);
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
        repository.saveInstructor(instructor);
        user.setInstructor(instructor);
        repository.saveUser(user);
    }

    private void addRoleStudent(Map<String, String> requestedBody, User user) {
        Degree degree = Degree.valueOf(Optional.of(requestedBody.get("degree")).orElseThrow().toUpperCase());
        Student student = Student.builder().degree(degree)
                .startDate(new Date()).build();
        user.setStudent(student);
        repository.saveStudent(student);
        repository.saveUser(user);
    }

    public String saveAndSendToken(String username) {
        Token token = Token.builder().username(username).token(generateToken()).build();
        return repository.saveToken(token).toString();
    }

    public void logout(Token token) {
        repository.deleteToken(token);
    }
}
