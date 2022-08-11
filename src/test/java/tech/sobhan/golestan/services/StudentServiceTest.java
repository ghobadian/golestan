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
import tech.sobhan.golestan.models.dto.CourseSectionDTO2;
import tech.sobhan.golestan.models.dto.InstructorDTO;
import tech.sobhan.golestan.models.dto.StudentAverageDTO;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.security.PasswordEncoder;

import java.util.ArrayList;
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

        when(repo.findTerm(anyLong())).thenReturn(term);
        Student student = repo.saveStudent(Student.builder().degree(Degree.BS).build());
        User user = User.builder().admin(false).active(true).phone("").nationalId("").username("student")
                .password(passwordEncoder.hash("student")).name("").student(student).build();
        when(repo.findUserByUsername(anyString())).thenReturn(user);
        when(repo.findStudentByUsername(anyString())).thenReturn(student);
        Course course = repo.saveCourse(Course.builder().title("course").units(3).build());
        Instructor instructor = repo.saveInstructor(Instructor.builder().rank(Rank.FULL).name("instructor").build());
        CourseSectionRegistration csr1 = createAndSaveCourseSection(term, student, course, instructor, 6.0);
        CourseSectionRegistration csr2 = createAndSaveCourseSection(term, student, course, instructor, 18.0);
        when(repo.findCSRsByStudentAndTerm(any(),any())).thenReturn(List.of(csr1,csr2));
        List<CourseSectionDTO2> courseSections = new ArrayList<>();
        courseSections.add(CourseSectionDTO2.builder().id(null).courseUnits(5).instructor(InstructorDTO.builder()
                .name("instructor1").rank(Rank.FULL).build()).courseName("math").score(18.00).build());
        courseSections.add(CourseSectionDTO2.builder().id(null).courseUnits(5).instructor(InstructorDTO.builder()
                .name("instructor2").rank(Rank.FULL).build()).courseName("physics").score(6.00).build());
    }

    private CourseSectionRegistration createAndSaveCourseSection(Term term, Student student, Course course, Instructor instructor, Double score) {
        CourseSection courseSection2 = repo.saveCourseSection(CourseSection.builder()
                .course(course).term(term).instructor(instructor).build());
        return CourseSectionRegistration.builder()
                .student(student).courseSection(courseSection2).score(score).build();
    }

    @SneakyThrows
    @Test
    void seeScoresInSpecifiedTerm() {
        User user = repo.findUserByUsername("student");
//        Term term = repo.findAllTerms().get(0);
        Term term = repo.findTerm(69L);
        EntityModel<StudentAverageDTO> response = studentService.seeScoresInSpecifiedTerm(term.getId(), user.getUsername());

        double avg = Objects.requireNonNull(response.getContent()).getAverage();;
        assertEquals(avg, 12);
    }
}