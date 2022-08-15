package tech.sobhan.golestan.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ContextConfiguration;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.enums.Degree;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.dto.StudentAverageDTO;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.security.PasswordEncoder;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@ContextConfiguration(classes = {StudentService.class, PasswordEncoder.class})
class StudentServiceTest {
    @Autowired
    private StudentService studentService;
    @MockBean
    private Repo repo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void loadData() {
        Term term = Term.builder().title("temp").open(true).build();
        Student student = Student.builder().degree(Degree.BS).build();
        User user = User.builder().admin(false).active(true).phone("").nationalId("").username("student")
                .password(passwordEncoder.hash("student")).name("").student(student).build();
        Course course = Course.builder().title("course").units(3).build();
        CourseSectionRegistration csr1 = createAndSaveCourseSection(term, student, course, 6.0);
        CourseSectionRegistration csr2 = createAndSaveCourseSection(term, student, course, 18.0);
        loadMockConfigs(term, student, user, csr1, csr2);
    }

    private void loadMockConfigs(Term term, Student student, User user, CourseSectionRegistration csr1, CourseSectionRegistration csr2) {
        when(repo.findTerm(anyLong())).thenReturn(term);
        when(repo.findUserByUsername(anyString())).thenReturn(user);
        when(repo.findStudentByUsername(anyString())).thenReturn(student);
        when(repo.findUsernameByToken(anyString())).thenReturn("student");
        when(repo.findCSRsByStudentAndTerm(any(),any())).thenReturn(List.of(csr1, csr2));
    }

    private CourseSectionRegistration createAndSaveCourseSection(Term term, Student student, Course course, Double score) {
        Instructor instructor = Instructor.builder().rank(Rank.FULL).user(User.builder().name("instructor").build()).build();
        CourseSection courseSection2 = CourseSection.builder()
                .course(course).term(term).instructor(instructor).build();
        return CourseSectionRegistration.builder()
                .student(student).courseSection(courseSection2).score(score).build();
    }

    @SneakyThrows
    @Test
    void seeScoresInSpecifiedTerm() {
        User user = repo.findUserByUsername("student");
        EntityModel<StudentAverageDTO> response = studentService.seeScoresInSpecifiedTerm(69L, user.getUsername());
        double avg = Objects.requireNonNull(response.getContent()).getAverage();
        assertEquals(avg, 12);
    }
}