package tech.sobhan.golestan.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.security.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class StudentServiceTest {
    @Autowired
    private StudentService studentService;
    @Autowired
    private Repo repo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void loadData() {
        Term term = repo.saveTerm(Term.builder().title("temp").open(true).build());
        Student student = repo.saveStudent(Student.builder().degree(Degree.BS).build());
        createAndSaveUser(student);
        Course course = repo.saveCourse(Course.builder().title("course").units(3).build());
        Instructor instructor = repo.saveInstructor(Instructor.builder().rank(Rank.FULL).build());
        createAndSaveCourseSection(term, student, course, instructor, 6.0);
        createAndSaveCourseSection(term, student, course, instructor, 18.0);
    }

    private void createAndSaveUser(Student student) {
        repo.saveUser(User.builder().admin(false).active(true).phone("").nationalId("").username("student")
                .password(passwordEncoder.hash("student")).name("").student(student).build());
    }

    private void createAndSaveCourseSection(Term term, Student student, Course course, Instructor instructor, Double score) {
        CourseSection courseSection2 = repo.saveCourseSection(CourseSection.builder()
                .course(course).term(term).instructor(instructor).build());
        repo.saveCourseSectionRegistration(CourseSectionRegistration.builder()
                .student(student).courseSection(courseSection2).score(score).build());
    }

    @SneakyThrows
    @Test
    void seeScoresInSpecifiedTerm() {
        User user = repo.findUserByUsername("student");
        Term term = repo.findAllTerms().get(0);
        StudentAverageDTO response = studentService.seeScoresInSpecifiedTerm(term.getId(), user.getUsername());
        double avg = response.getAverage();
        assertEquals(avg, 12);
    }
}