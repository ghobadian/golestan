package tech.sobhan.golestan.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.business.exceptions.UserNotFoundException;
import tech.sobhan.golestan.repositories.InstructorRepository;
import tech.sobhan.golestan.repositories.StudentRepository;
import tech.sobhan.golestan.repositories.UserRepository;

import java.util.List;

import static tech.sobhan.golestan.security.config.PasswordConfiguration.getPasswordEncoder;
import static tech.sobhan.golestan.utils.Util.createLog;
import static tech.sobhan.golestan.utils.Util.deleteLog;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;//todo find usage
    private final InstructorRepository instructorRepository;

    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository, InstructorRepository instructorRepository) {
        this.passwordEncoder = getPasswordEncoder();
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    public List<User> list() {
        //        if(allUsers.isEmpty()){
//            throw new UserNotFoundException();
//        }
        return userRepository.findAll();
    }

    public User create(String username, String password, String name, String phone, String nationalId) {
        User user = User.builder().username(username).password(passwordEncoder.encode(password)).name(name).phone(phone)
                .nationalId(nationalId).active(false).admin(false).build();
        return create(user);
    }

    public User create(User user) {
        if (userExists(list(), user)) return null;
        createLog(User.class, user.getId());
        return userRepository.save(user);
    }

    public User read(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
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

    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        studentRepository.delete(user.getStudent());
        instructorRepository.delete(user.getInstructor());
        userRepository.delete(user);
        deleteLog(User.class, id);
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
