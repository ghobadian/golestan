package tech.sobhan.golestan;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.enums.Degree;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.security.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static tech.sobhan.golestan.constants.ApiPaths.COURSE_SECTION_CREATE_PATH;


@Component
@RequiredArgsConstructor
public class Loader {
    private Repo repo;
    private MockMvc mockMvc;
    private final PasswordEncoder passwordEncoder;

    @SneakyThrows
    public void loadData(Repo repo, MockMvc mockMvc) {
        this.repo = repo;
        this.mockMvc = mockMvc;
        loadAdmin();
        createStudentUsers();
        createInstructorUsers();
        giveStudentRoles();
        giveInstructorRoles();
        createTerms();
        createCourse();
        createCourseSection();
    }

    private void loadAdmin() {
        if(!repo.userExistsByUsername("admin")) {
            User admin = User.builder().username("admin").password("admin").name("admin").phone("1234")
                    .nationalId("12345465489").admin(true).active(true).build();
            admin.setPassword(passwordEncoder.hash(admin.getPassword()));
            repo.saveUser(admin);
        }
        repo.saveToken("admin","admin");
    }

    @SneakyThrows
    private void createCourseSection() {
        for(int i=0;i<10;i++) {
            Course course = repo.findAllCourses().get(i);
            Instructor instructor = repo.findAllInstructors().get(i);
            Term term = repo.findAllTerms().get(i);
            repo.saveToken("instructor"+i, "instructor"+i);
            mockMvc.perform(post(COURSE_SECTION_CREATE_PATH)
                    .header("token", "instructor" + i)
                    .param("courseId", String.valueOf(course.getId()))
                    .param("instructorId", String.valueOf(instructor.getId()))
                    .param("termId", String.valueOf(term.getId())));
            CourseSection courseSection = repo.findAllCourseSections().get(i);
            assertEquals(courseSection.getCourse(), course);
            assertEquals(courseSection.getInstructor(), instructor);
            assertEquals(courseSection.getTerm(), term);
        }
    }

    @SneakyThrows
    private void createCourse() {
        for(int i=0;i<10;i++) {
            mockMvc.perform(post("/management/courses")
                    .header("token", "admin")
                    .param("title", "BodyBuilding" + i)
                    .param("units", String.valueOf(5)));
        }
        Course course = repo.findAllCourses().get(0);
        assertEquals(course.getTitle(), "BodyBuilding0");
        assertEquals(course.getUnits(), 5);
    }

    @SneakyThrows
    private void createTerms() {
        for(int i=3;i<13;i++) {
            mockMvc.perform(post("/management/terms")
                    .header("token", "admin")
                    .param("title", "400" + i)
                    .param("open", String.valueOf(true)));
        }

        Term term = repo.findAllTerms().get(0);
        assertEquals(term.getTitle(), "4003");
    }

    @SneakyThrows
    private void giveInstructorRoles() {
        for(int i=0;i<10;i++) {
            User instructorUser = repo.findUserByUsername("instructor" + i);
            JSONObject requestBody = new JSONObject();
            requestBody.put("role", "instructor");
            requestBody.put("rank", "FULL");
            mockMvc.perform(post("/management/users/{userId}/modifyRole",instructorUser.getId())
                    .header("token", "admin")
                    .contentType(MediaType.APPLICATION_JSON).content(requestBody.toString()));
        }
        Instructor instructor = repo.findAllInstructors().get(0);
        assertEquals(instructor.getRank(), Rank.FULL);
    }

    @SneakyThrows
    private void giveStudentRoles() {
        for(int i=0;i<10;i++) {
            User studentUser = repo.findUserByUsername("student" + i);
            JSONObject requestBody = new JSONObject();
            requestBody.put("role", "student");
            requestBody.put("degree", "BS");
            mockMvc.perform(post("/management/users/{userId}/modifyRole",studentUser.getId())
                    .header("token", "admin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(String.valueOf(requestBody))
                    .accept(MediaType.APPLICATION_JSON));
        }
        Student student = repo.findStudentByUsername("student0");
        assertEquals(student.getDegree(), Degree.BS);
    }

    private void createInstructorUsers() throws Exception {
        for(int i=0;i<10;i++) {
            mockMvc.perform(post("/users")
                    .param("username", "instructor" + i)
                    .param("password", "instructor" + i)
                    .param("name", "instructor" + i)
                    .param("phone", "0903156879" + i)
                    .param("nationalId", "546879412" + i));
            repo.saveToken("instructor" + i, "instructor" + i);
        }
        User instructor = repo.findUserByUsername("instructor3");
        assertEquals(instructor.getPassword() , passwordEncoder.hash("instructor3"));
        assertEquals(repo.findAllUsers().size(), 21);
    }

    private void createStudentUsers() throws Exception {
        for(int i=0;i<10;i++) {
            mockMvc.perform(post("/users")
                    .param("username", "student" + i)
                    .param("password", "student" + i)
                    .param("name", "student"+ i)
                    .param("phone", "0901254125" + i)
                    .param("nationalId", "541254687" + i));
            repo.saveToken("student" + i, "student" + i);
        }
        assertEquals(11, repo.findAllUsers().size());
    }
}
