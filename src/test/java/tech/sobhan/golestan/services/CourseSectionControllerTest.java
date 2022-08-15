package tech.sobhan.golestan.services;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.sobhan.golestan.Loader;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static tech.sobhan.golestan.constants.ApiPaths.COURSE_SECTION_LIST_PATH;

@WebAppConfiguration
@SpringBootTest
class CourseSectionControllerTest {
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
    void list() {
        Term term = repo.findAllTerms().get(0);
        CourseSection cs = repo.findAllCourseSections().get(0);
        MvcResult result = mockMvc.perform(get(COURSE_SECTION_LIST_PATH)
                .header("token", "admin")
                .param("termId", String.valueOf(term.getId()))
                .param("instructorName", "instructor0")
                .param("courseName", "BodyBuilding0")
                .param("pageNumber", String.valueOf(0))
                .param("maxInEachPage", String.valueOf(5))
        ).andReturn();
        JSONArray jsonArray = new JSONArray(result.getResponse().getContentAsString());
        JSONObject actualResult = (JSONObject) jsonArray.get(0);
        assertEquals(Long.parseLong(String.valueOf(actualResult.get("id"))), cs.getId());
        assertEquals(((JSONObject) actualResult.get("term")).get("title"), cs.getTerm().getTitle());
        Object actualInstructorId = ((JSONObject) actualResult.get("instructor")).get("id");
        assertEquals(Long.parseLong(String.valueOf(actualInstructorId)), cs.getInstructor().getId());
    }
}