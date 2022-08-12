package tech.sobhan.golestan.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import tech.sobhan.golestan.dao.Repo;
import tech.sobhan.golestan.models.Course;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = CourseService.class)
class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @MockBean
    private Repo repo;

    @Test
    void list() {
        Course c1 = Course.builder().units(5).title("course5").build();
        Course c2 = Course.builder().units(2).title("course2").build();
        List<Course> courses = List.of(c1, c2);
        when(repo.findAllCourses()).thenReturn(courses);
        assertArrayEquals(courseService.list().toArray(), courses.toArray());
    }
}