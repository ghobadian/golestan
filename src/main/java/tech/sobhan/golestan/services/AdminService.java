package tech.sobhan.golestan.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.business.exceptions.UserNotFoundException;
import tech.sobhan.golestan.enums.Degree;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.repositories.InstructorRepository;
import tech.sobhan.golestan.repositories.StudentRepository;
import tech.sobhan.golestan.repositories.UserRepository;
import tech.sobhan.golestan.security.Role;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AdminService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    public AdminService(UserService userService, UserRepository userRepository, StudentRepository studentRepository,
                        InstructorRepository instructorRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    public void modifyRole(Long id, Map<String, String> requestedBody) {
        log.warn("AdminService/modifyRole");
        User foundUser = (userRepository.findById(id).orElseThrow(UserNotFoundException::new));
        foundUser.setActive(true);
        Role role = Role.valueOf(requestedBody.get("role").toUpperCase());
        switch (role){
            case STUDENT -> addRoleStudent(id, requestedBody, foundUser);
            case INSTRUCTOR -> addRoleInstructor(id, requestedBody, foundUser);
        }
        log.warn("AdminService/modifyRole2");
    }

    private void addRoleInstructor(Long id, Map<String, String> requestedBody, User foundUser) {
        Instructor instructor = Instructor.builder().rank(Rank.valueOf(requestedBody.get("rank").toUpperCase())).build();
        instructorRepository.save(instructor);
        foundUser.setInstructor(instructor);
        userService.update(foundUser, id);
//        userRepository.findById(id).map(user -> {
//            user = foundUser;
//            return userRepository.save(user);
//        });
    }

    private void addRoleStudent(Long id, Map<String, String> requestedBody, User foundUser) {
        log.warn("AdminService/addRoleStudent");
        Degree degree = Degree.valueOf(Optional.of(requestedBody.get("degree")).orElseThrow().toUpperCase());
        Student student = Student.builder().degree(degree)
                .startDate(new Date()).build();
        foundUser.setStudent(student);
        studentRepository.save(student);
        userService.update(foundUser, id);
        log.warn("AdminService/addRoleStudent2");

//        userRepository.findById(id).map(user -> {
//            user = foundUser;
//            return userRepository.save(user);
//        });
    }
}
