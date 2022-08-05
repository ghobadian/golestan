package tech.sobhan.golestan;

import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import tech.sobhan.golestan.business.exceptions.UserNotFoundException;
import tech.sobhan.golestan.enums.Degree;
import tech.sobhan.golestan.enums.Rank;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;
import tech.sobhan.golestan.models.users.Student;
import tech.sobhan.golestan.models.users.User;
import tech.sobhan.golestan.repositories.RepositoryHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class Loader {
    private RepositoryHandler repositoryHandler;
    private MockMvc mockMvc;
    @SneakyThrows
    public void loadData(RepositoryHandler repositoryHandler, MockMvc mockMvc) {
        this.repositoryHandler = repositoryHandler;
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
        try{
            repositoryHandler.findUserByUsername("admin");
        }catch (UserNotFoundException e){
            User admin = User.builder().username("admin").password("admin").name("admin").phone("1234")
                    .nationalId("1234").admin(true).active(true).build();
            repositoryHandler.saveUser(admin);
        }
    }

    @SneakyThrows
    private void createCourseSection() {
        for(int i=0;i<10;i++){
            Course course = repositoryHandler.findAllCourses().get(i);
            Instructor instructor = repositoryHandler.findAllInstructors().get(i);
            Term term = repositoryHandler.findAllTerms().get(i);
            mockMvc.perform(post("/courseSections")
                    .header("username", "instructor" + i)
                    .header("password", "instructor" + i)
                    .param("courseId", String.valueOf(course.getId()))
                    .param("instructorId", String.valueOf(instructor.getId()))
                    .param("termId", String.valueOf(term.getId())));
            CourseSection courseSection = repositoryHandler.findAllCourseSections().get(i);
            assertEquals(courseSection.getCourse(), course);
            assertEquals(courseSection.getInstructor(), instructor);
            assertEquals(courseSection.getTerm(), term);
        }
    }

    @SneakyThrows
    private void createCourse() {
        for(int i=0;i<10;i++){
            mockMvc.perform(post("/management/courses")
                    .header("username", "admin")
                    .header("password", "admin")
                    .param("title", "BodyBuilding" + i)
                    .param("units", String.valueOf(5)));
        }
        Course course = repositoryHandler.findAllCourses().get(0);
        assertEquals(course.getTitle(), "BodyBuilding0");
        assertEquals(course.getUnits(), 5);
    }

    @SneakyThrows
    private void createTerms() {
        for(int i=3;i<13;i++){
            mockMvc.perform(post("/management/terms")
                    .header("username", "admin")
                    .header("password", "admin")
                    .param("title", "400" + i)
                    .param("open", String.valueOf(true)));
        }

        Term term = repositoryHandler.findAllTerms().get(0);
        assertEquals(term.getTitle(), "4003");
    }

    @SneakyThrows
    private void giveInstructorRoles() {
        for(int i=0;i<10;i++){
            User instructorUser = repositoryHandler.findUserByUsername("instructor" + i);
            JSONObject requestBody = new JSONObject();
            requestBody.put("role", "instructor");
            requestBody.put("rank", "FULL");
            mockMvc.perform(post("/management/users/{userId}/modifyRole",instructorUser.getId())
                    .header("username", "admin")
                    .header("password", "admin")
                    .contentType(MediaType.APPLICATION_JSON).content(requestBody.toString()));
        }
        Instructor instructor = repositoryHandler.findAllInstructors().get(0);
        assertEquals(instructor.getRank(), Rank.FULL);
    }

    @SneakyThrows
    private void giveStudentRoles() {
        for(int i=0;i<10;i++){
            User studentUser = repositoryHandler.findUserByUsername("student" + i);
            JSONObject requestBody = new JSONObject();
            requestBody.put("role", "student");
            requestBody.put("degree", "BS");
            mockMvc.perform(post("/management/users/{userId}/modifyRole",studentUser.getId())
                    .header("username", "admin")
                    .header("password", "admin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(String.valueOf(requestBody)).accept(MediaType.APPLICATION_JSON));
        }
        Student student = repositoryHandler.findStudentByUsername("student0");
        assertEquals(student.getDegree(), Degree.BS);
    }

    private void createInstructorUsers() throws Exception {
        for(int i=0;i<10;i++){
            mockMvc.perform(post("/users")
                    .param("username", "instructor" + i)
                    .param("password", "instructor" + i)
                    .param("name", "instructor" + i)
                    .param("phone", "0903156879" + i)
                    .param("nationalId", "546879412" + i));
        }
        User instructor = repositoryHandler.findUserByUsername("instructor3");
        assertEquals(instructor.getPassword() , "instructor3");
        assertEquals(repositoryHandler.findAllUsers().size(), 21);
    }

    private void createStudentUsers() throws Exception {
        for(int i=0;i<10;i++){
            mockMvc.perform(post("/users")
                    .param("username", "student" + i)
                    .param("password", "student" + i)
                    .param("name", "student"+ i)
                    .param("phone", "0901254125" + i)
                    .param("nationalId", "" + i));
        }
        assertEquals(11, repositoryHandler.findAllUsers().size());
    }
}
