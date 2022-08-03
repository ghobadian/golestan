package tech.sobhan.golestan;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.sobhan.golestan.business.exceptions.UserNotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
//@AutoConfigureDataJdbc
@SpringBootTest/*(properties = "tech.sobhan.aplicationTest.properties")*/
public class IntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private final RepositoryHandler repositoryHandler = Mockito.mock(RepositoryHandler.class);
    private static MockMvc mockMvc;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        try{
            repositoryHandler.findUserByUsername("admin");
        }catch (UserNotFoundException e){
            User admin = User.builder().username("admin").password("admin").name("admin").phone("1234")
                    .nationalId("1234").admin(true).active(true).build();
            repositoryHandler.saveUser(admin);
        }
    }

//    @AfterAll
//    public static void finisher(RepositoryHandler repositoryHandler){
//        repositoryHandler.deleteAll();
//    }

    @SneakyThrows
    @Test
    public void sth(){
        createStudent();
        createInstructor();
        giveStudentRole();
        giveInstructorRole();
        createTerm();
        createCourse();
        createCourseSection();
        signUpCourseSection();
        giveMarkToStudent();
        seeScoresInTerm();
        repositoryHandler.deleteAll();
    }

    @SneakyThrows
    private void seeScoresInTerm() {
        Term term = repositoryHandler.findAllTerms().get(0);
        String expectedResponse = "[{\"average\":19.75},{\"course_section_id\":8," +
                "\"course_name\":\"BodyBuilding\",\"course_units\":5," +
                "\"instructor\":\"Instructor(id=5, rank=FULL)\",\"score\":19.75}]";
        this.mockMvc.perform(get("/student/see_scores/")
                .header("username", "student")
                .header("password", "student")
                .param("termId", String.valueOf(term.getId())))
                .andExpect(content().string(expectedResponse));
    }

    @SneakyThrows
    private void giveMarkToStudent() {
        Long studentId = repositoryHandler.findUserByUsername("student").getStudent().getStudentId();
        CourseSection courseSection = repositoryHandler.findAllCourseSections().get(0);
        mockMvc.perform(post("/instructor/give_mark/{studentId}", studentId)
                .header("username", "instructor")
                .header("password", "instructor")
                .param("courseSectionId", String.valueOf(courseSection.getId()))
//                .param("studentId", String.valueOf(studentId))
                .param("score", String.valueOf(19.75)));
        CourseSectionRegistration courseSectionRegistration = repositoryHandler
                .findCourseSectionRegistrationByCourseSectionAndStudent(courseSection.getId(), studentId);
        assertEquals(courseSectionRegistration.getScore(), 19.75);
    }

    @SneakyThrows
    private void signUpCourseSection() {
        CourseSection courseSection = repositoryHandler.findAllCourseSections().get(0);
        mockMvc.perform(post("/student/sign_up_section")
                .header("username", "student")
                .header("password", "student")
                .param("courseSectionId", String.valueOf(courseSection.getId())));
        CourseSectionRegistration courseSectionRegistration = repositoryHandler
                .findCourseSectionRegistrationByCourseSection(courseSection.getId()).get(0);
        Student student = repositoryHandler.findStudentByUsername("student");
        assertEquals(courseSectionRegistration.getStudent().getStudentId(), student.getStudentId());
    }

    @SneakyThrows
    private void createCourseSection() {
        Course course = repositoryHandler.findAllCourses().get(0);
        Instructor instructor = repositoryHandler.findAllInstructors().get(0);
        Term term = repositoryHandler.findAllTerms().get(0);
        mockMvc.perform(post("/courseSections")
                .header("username", "instructor")
                .header("password", "instructor")
                .param("courseId", String.valueOf(course.getId()))
                .param("instructorId", String.valueOf(instructor.getId()))
                .param("termId", String.valueOf(term.getId())));
        CourseSection courseSection = repositoryHandler.findAllCourseSections().get(0);
        assertEquals(courseSection.getCourse(), course);
        assertEquals(courseSection.getInstructor(), instructor);
        assertEquals(courseSection.getTerm(), term);
    }

    @SneakyThrows
    private void createCourse() {
        mockMvc.perform(post("/management/courses")
                .header("username", "admin")
                .header("password", "admin")
                .param("title", "BodyBuilding")
                .param("units", String.valueOf(5)));//todo type is changed to string therefore sus
        Course course = repositoryHandler.findAllCourses().get(0);
        assertEquals(course.getTitle(), "BodyBuilding");
        assertEquals(course.getUnits(), 5);
    }

    @SneakyThrows
    private void createTerm() {
        mockMvc.perform(post("/management/terms")
                .header("username", "admin")
                .header("password", "admin")
                .param("title", "4003")
                .param("open", String.valueOf(true)));//todo sus
        Term term = repositoryHandler.findAllTerms().get(0);
        assertEquals(term.getTitle(), "4003");
    }

    @SneakyThrows
    private void giveInstructorRole() {
        User instructorUser = repositoryHandler.findUserByUsername("instructor");
        JSONObject requestBody = new JSONObject();
        requestBody.put("role", "instructor");
        requestBody.put("rank", "FULL");
        mockMvc.perform(post("/management/users/{userId}/modifyRole",instructorUser.getId())
                .header("username", "admin")
                .header("password", "admin")
                .contentType(MediaType.APPLICATION_JSON).content(requestBody.toString()));
        Instructor instructor = repositoryHandler.findAllInstructors().get(0);
        assertEquals(instructor.getRank(), Rank.FULL);
    }

    @Autowired private ObjectMapper mapper;
    @SneakyThrows//todo remove if in main
    private void giveStudentRole() {
        User studentUser = repositoryHandler.findUserByUsername("student");
        JSONObject requestBody = new JSONObject();
        requestBody.put("role", "student");
        requestBody.put("degree", "BS");
        mockMvc.perform(post("/management/users/{userId}/modifyRole",studentUser.getId())
                .header("username", "admin")
                .header("password", "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestBody)).accept(MediaType.APPLICATION_JSON));
        Student student = repositoryHandler.findStudentByUsername("student");
        assertEquals(student.getDegree(), Degree.BS);
    }

    private void createInstructor() throws Exception {
        mockMvc.perform(post("/users")
                .param("username", "instructor")
                .param("password", "instructor")
                .param("name", "instructor")
                .param("phone", "09031568795")//todo add regex for phone
                .param("nationalId", "5468794124"));
        User instructor = repositoryHandler.findUserByUsername("instructor");
        assertEquals(instructor.getPassword(), "instructor");
    }

    private void createStudent() throws Exception {
        mockMvc.perform(post("/users")
                .param("username", "student")
                .param("password", "student")
                .param("name", "student")
                .param("phone", "09031568794")//todo add regex for phone
                .param("nationalId", "5468795124"));
        User student = repositoryHandler.findUserByUsername("student");
        assertEquals(student.getPassword(),("student"));
    }


}
