package tech.sobhan.golestan.services;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.business.exceptions.UserNotFoundException;
import tech.sobhan.golestan.repositories.InstructorRepository;
import tech.sobhan.golestan.repositories.StudentRepository;
import tech.sobhan.golestan.repositories.UserRepository;
import tech.sobhan.golestan.security.ErrorChecker;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;
import static tech.sobhan.golestan.utils.Util.deleteLog;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;//todo find usage
    private final InstructorRepository instructorRepository;
    private final ErrorChecker errorChecker;


    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       InstructorRepository instructorRepository,
                       ErrorChecker errorChecker) {
        this.errorChecker = errorChecker;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    public String list(String username, String password) {
        errorChecker.checkIsUser(username, password);
        return list().toString();
    }

    private List<User> list() {
        return userRepository.findAll();
    }

    public User create(String username, String password, String name, String phone, String nationalId) {
        User user = User.builder().username(username).password(password).name(name).phone(phone)//todo encode password later
                .nationalId(nationalId).build();
        return create(user);
    }

    public User create(User user) {
        if (userExists(list(), user)) return null;
        user.setActive(false);
        user.setAdmin(false);
        createLog(User.class, user.getId());
        return userRepository.save(user);
    }

    public String read(Long id, String username, String password) {
        errorChecker.checkIsUser(username, password);
        return read(id).toString();
    }

    private User read(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public String update(User newUser, Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        return update(newUser, id).toString();
    }

    public User update(User newUser, Long id) {
        userRepository.findById(id).map(user -> {
            user = newUser;
            return userRepository.save(user);
        }).orElseGet(() -> {
            newUser.setId(id);
            return userRepository.save(newUser);
        });
        return newUser;
    }

    public String delete(Long id, String username, String password) {
        errorChecker.checkIsAdmin(username, password);
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        studentRepository.delete(user.getStudent());
        instructorRepository.delete(user.getInstructor());
        userRepository.delete(user);
        deleteLog(User.class, id);
        return "OK";
    }

    private static boolean userExists(List<User> allUsers, User user) {
        for (User u : allUsers) {
            if(user.equals(u)){
                System.out.println("ERROR403 duplicate Users");
                return true;
            }
        }
        return false;
    }
}
