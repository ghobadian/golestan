package tech.sobhan.golestan.services;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.RepositoryHandler;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.security.PasswordEncoder.hash;
import static tech.sobhan.golestan.utils.Util.createLog;
import static tech.sobhan.golestan.utils.Util.deleteLog;

@Service
public class UserService {
    private final RepositoryHandler repositoryHandler;
    private final InstructorService instructorService;
    private final StudentService studentService;
    private final ErrorChecker errorChecker;


    public UserService(RepositoryHandler repositoryHandler,
                       InstructorService instructorService,
                       StudentService studentService,
                       ErrorChecker errorChecker) {
        this.repositoryHandler = repositoryHandler;
        this.instructorService = instructorService;
        this.studentService = studentService;
        this.errorChecker = errorChecker;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return list().toString();
    }

    private List<User> list() {
        return repositoryHandler.findAllUsers();
    }

    public User create(String username, String password, String name, String phone, String nationalId) {
        errorChecker.checkPhoneNumber(phone);
        errorChecker.checkNationalId(nationalId);
        errorChecker.checkUserExists(username, phone, nationalId);
        User user = User.builder().username(username).password(password)
                .name(name).phone(phone).nationalId(nationalId).build();
        return create(user);
    }

    public User create(User user) {
        user.setActive(false);
        user.setAdmin(false);
        createLog(User.class, user.getId());
        user.setPassword(hash(user.getPassword()));
        return repositoryHandler.saveUser(user);
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private User read(Long id) {
        return repositoryHandler.findUser(id);
    }

    public String update(String name, String newUsername, String newPassword, String phone,
                         String username, String password) {
        errorChecker.checkIsUser(username, password);
        return update(name, newUsername, newPassword, phone, username).toString();
    }

    public User update(String name, String newUsername, String newPassword, String phone, String username) {
        User user = repositoryHandler.findUserByUsername(username);
        updateName(name, user);
        updateUsername(newUsername, username, user);
        updatePassword(newPassword, user);
        updatePhoneNumber(phone, user);
        return repositoryHandler.saveUser(user);
    }

    private void updatePhoneNumber(String phone, User user) {
        if(phone !=null && !repositoryHandler.userExistsByPhone(phone)){
            user.setPhone(phone);
        }
    }

    private void updatePassword(String newPassword, User user) {
        if(newPassword !=null){
            user.setPassword(hash(newPassword));
        }
    }

    private void updateUsername(String newUsername, String username, User user) {
        if(newUsername !=null && !repositoryHandler.userExistsByUsername(username)){
            user.setUsername(newUsername);
        }
    }

    private void updateName(String name, User user) {
        if(name !=null){
            user.setName(name);
        }
    }

    public void delete(Long id, String username, String password) {//todo check
        errorChecker.checkIsAdmin(username, password);
        User user = repositoryHandler.findUser(id);
        deleteInstructorOfUser(user);
        deleteStudentOfUser(user);
        repositoryHandler.deleteUser(user);
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
}
