package tech.sobhan.golestan.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import tech.sobhan.golestan.enums.Degree;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.RepositoryHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StudentServiceTest {
    @Autowired
    private final StudentService studentService = Mockito.mock(StudentService.class);

    @Autowired
    private final RepositoryHandler repositoryHandler = Mockito.mock(RepositoryHandler.class);

    private static Term term;
    private  static User user;

    @BeforeEach
    public void loadData(){
        if(repositoryHandler.findAllUsers().isEmpty()){
            loadDatas(repositoryHandler);
        }
    }
    public static void loadDatas(RepositoryHandler repositoryHandler){
        term = repositoryHandler.saveTerm(Term.builder().title("temp").open(true).build());
        Student student = repositoryHandler.saveStudent(Student.builder()
                .degree(Degree.BS)
                .build());
        user = repositoryHandler.saveUser(User.builder()
                .admin(false)
                .active(true)
                .phone("")
                .nationalId("")
                .password("student")
                .username("student")
                .name("")
                .student(student)
                .build());
        Course course = repositoryHandler.saveCourse(Course.builder().title("course").units(3).build());
        Instructor instructor = repositoryHandler.saveInstructor(Instructor.builder().rank(Rank.FULL).build());
        CourseSection courseSection = repositoryHandler.saveCourseSection(CourseSection.builder()
                .course(course).term(term).instructor(instructor).build());
        repositoryHandler.saveCourseSectionRegistration(CourseSectionRegistration.builder()
                .student(student).courseSection(courseSection).score(6).build());
        CourseSection courseSection2 = repositoryHandler.saveCourseSection(CourseSection.builder()
                .course(course).term(term).instructor(instructor).build());
        repositoryHandler.saveCourseSectionRegistration(CourseSectionRegistration.builder()
                .student(student).courseSection(courseSection2).score(18).build());
    }
    @SneakyThrows
    @Test
    void seeScoresInSpecifiedTerm() {

        JSONArray response = studentService.seeScoresInSpecifiedTerm(term.getId(), user.getUsername(), user.getPassword());
        double avg = (Double) ((JSONObject) response.get(0)).get("average");
        assertEquals(avg, 12);
    }
}