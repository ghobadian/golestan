package tech.sobhan.golestan;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.dto.CourseSectionDTO2;
import tech.sobhan.golestan.models.dto.InstructorDTO;
import tech.sobhan.golestan.models.dto.StudentAverageDTO;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static tech.sobhan.golestan.constants.ApiPaths.SEE_SCORES_IN_TERM_PATH;

@WebAppConfiguration
@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class IntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private Repo repo;
    @Autowired
    private Loader loader;
    private static MockMvc mockMvc;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        loader.loadData(repo, mockMvc);
    }

    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
    }

    @SneakyThrows
    @Test
    public void projectIntegrationScenario( ) {
        signUpCourseSection();
        giveMarkToStudent();
        seeScoresInTerm();
    }

    @SneakyThrows
    private void seeScoresInTerm() {
        Term term = repo.findAllTerms().get(0);
        Instructor instructor = repo.findAllInstructors().get(0);
        CourseSection  courseSection = repo.findAllCourseSections().get(0);
        CourseSectionDTO2 sth = CourseSectionDTO2.builder().courseUnits(5).id(courseSection.getId())
                .courseName(courseSection.getCourse().getTitle())
                .instructor(InstructorDTO.builder().rank(instructor
                        .getRank()).name("instructor0").build()).build();
        StudentAverageDTO expectedResponse = StudentAverageDTO.builder()
                .average(19.75).courseSections(List.of(sth)).build();
        MvcResult response = mockMvc.perform(get(SEE_SCORES_IN_TERM_PATH)
                .header("token", "student0")
                .param("termId", String.valueOf(term.getId())))
                .andReturn();
        JSONObject json = new JSONObject(response.getResponse().getContentAsString());
        assertEquals(expectedResponse.getAverage(), json.get("average"));
        JSONArray jArray = (JSONArray) json.get("courseSections");
        JSONObject dto = (JSONObject) jArray.get(0);
        assertEquals(expectedResponse.getCourseSections().get(0).getCourseName(), dto.get("courseName"));
    }

    @SneakyThrows
    private void giveMarkToStudent() {
        Student student = repo.findUserByUsername("student0").getStudent();
        CourseSection courseSection = repo.findAllCourseSections().get(0);
        mockMvc.perform(post("/instructor/give_mark/{studentId}", student.getId())
                .header("token", "instructor0")
                .param("courseSectionId", String.valueOf(courseSection.getId()))
                .param("score", String.valueOf(19.75)));
        CourseSectionRegistration courseSectionRegistration = repo
                .findCourseSectionRegistrationByCourseSectionAndStudent(courseSection, student);
        assertEquals(courseSectionRegistration.getScore(), 19.75);
    }

    @SneakyThrows
    private void signUpCourseSection() {
        CourseSection courseSection = repo.findAllCourseSections().get(0);
        mockMvc.perform(post("/student/sign_up_section")
                .header("token", "student0")
                .param("courseSectionId", String.valueOf(courseSection.getId())));
        CourseSectionRegistration courseSectionRegistration = repo
                .findCourseSectionRegistrationByCourseSection(courseSection).get(0);
        Student student = repo.findStudentByUsername("student0");
        assertEquals(courseSectionRegistration.getStudent().getId(), student.getId());
    }

    @SneakyThrows
    @Test
    public void testForGivingMultipleMarks() {
        createCourseSectionRegistrations();
        JSONArray expectedScores = new JSONArray(List.of(1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,20.0));
        CourseSection courseSection = repo.findAllCourseSections().get(0);
        JSONArray studentIds = new JSONArray(repo.findAllStudents()
                .stream()
                .map(Student::getId)
                .collect(Collectors.toList()));
        mockMvc.perform(post("/instructor/give_mark")
                .param("courseSectionId", String.valueOf(courseSection.getId()))
                .param("studentIds", String.valueOf(studentIds))
                .param("scores", String.valueOf(expectedScores))
                .header("token", "instructor0"));
        List<CourseSectionRegistration> courseSectionRegistrations = repo
                .findCourseSectionRegistrationByCourseSection(courseSection);
        JSONArray actualScores = new JSONArray(courseSectionRegistrations
                .stream()
                .map(CourseSectionRegistration::getScore).collect(Collectors.toList()));
        assertEquals(expectedScores.toString(),actualScores.toString());
    }

    private void createCourseSectionRegistrations() {
        for (CourseSection courseSection : repo.findAllCourseSections()) {
            for(int i=0;i<10;i++) {
                CourseSectionRegistration courseSectionRegistration = CourseSectionRegistration.builder()
                        .courseSection(courseSection)
                        .student(repo.findStudentByUsername("student" + i))
                        .build();
                repo.saveCourseSectionRegistration(courseSectionRegistration);
            }
        }
    }
}
