package tech.sobhan.golestan;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.repositories.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class IntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private Repository repository;
    @Autowired
    private Loader loader;
    private static MockMvc mockMvc;
    @SneakyThrows
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        loader.loadData(repository, mockMvc);
    }

    @AfterEach
    public void clearDatabase(){
        repository.deleteAll();
    }

    @SneakyThrows
    @Test
    public void projectIntegrationScenario(){
        signUpCourseSection();
        giveMarkToStudent();
        seeScoresInTerm();
    }

    @SneakyThrows
    private void seeScoresInTerm() {
        Term term = repository.findAllTerms().get(0);
        Instructor instructor = repository.findAllInstructors().get(0);
        CourseSection  courseSection = repository.findAllCourseSections().get(0);
        String expectedResponse = "[{\"average\":19.75},{\"course_section_id\":"+courseSection.getId()+"," +
                "\"course_name\":\"BodyBuilding0\",\"course_units\":5," +
                "\"instructor\":\""+instructor+"\",\"score\":19.75}]";
        mockMvc.perform(get("/student/see_scores/")
                .header("token", "student0")
                .param("termId", String.valueOf(term.getId())))
                .andExpect(content().string(expectedResponse));
    }

    @SneakyThrows
    private void giveMarkToStudent() {
        Student student = repository.findUserByUsername("student0").getStudent();
        CourseSection courseSection = repository.findAllCourseSections().get(0);
        mockMvc.perform(post("/instructor/give_mark/{studentId}", student.getId())
                .header("token", "instructor0")
                .param("courseSectionId", String.valueOf(courseSection.getId()))
                .param("score", String.valueOf(19.75)));
        CourseSectionRegistration courseSectionRegistration = repository
                .findCourseSectionRegistrationByCourseSectionAndStudent(courseSection, student);
        assertEquals(courseSectionRegistration.getScore(), 19.75);
    }

    @SneakyThrows
    private void signUpCourseSection() {
        CourseSection courseSection = repository.findAllCourseSections().get(0);
        mockMvc.perform(post("/student/sign_up_section")
                .header("token", "student0")
                .param("courseSectionId", String.valueOf(courseSection.getId())));
        CourseSectionRegistration courseSectionRegistration = repository
                .findCourseSectionRegistrationByCourseSection(courseSection).get(0);
        Student student = repository.findStudentByUsername("student0");
        assertEquals(courseSectionRegistration.getStudent().getId(), student.getId());
    }

    @SneakyThrows
    @Test
    public void testForGivingMultipleMarks(){
        createCourseSectionRegistrations();
        JSONArray expectedScores = new JSONArray(List.of(1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,20.0));
        CourseSection courseSection = repository.findAllCourseSections().get(0);
        JSONArray studentIds = new JSONArray(repository.findAllStudents()
                .stream()
                .map(Student::getId)
                .collect(Collectors.toList()));
        mockMvc.perform(post("/instructor/give_mark")
                .param("courseSectionId", String.valueOf(courseSection.getId()))
                .param("studentIds", String.valueOf(studentIds))
                .param("scores", String.valueOf(expectedScores))
                .header("token", "instructor0"));
        List<CourseSectionRegistration> courseSectionRegistrations = repository
                .findCourseSectionRegistrationByCourseSection(courseSection);
        JSONArray actualScores = new JSONArray(courseSectionRegistrations
                .stream()
                .map(CourseSectionRegistration::getScore).collect(Collectors.toList()));
        assertEquals(expectedScores.toString(),actualScores.toString());
    }

    private void createCourseSectionRegistrations() {
        for (CourseSection courseSection : repository.findAllCourseSections()) {
            for(int i=0;i<10;i++){
                CourseSectionRegistration courseSectionRegistration = CourseSectionRegistration.builder()
                        .courseSection(courseSection)
                        .student(repository.findStudentByUsername("student" + i))
                        .build();
                repository.saveCourseSectionRegistration(courseSectionRegistration);
            }
        }

    }


}
