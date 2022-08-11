package tech.sobhan.golestan.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import tech.sobhan.golestan.models.Course;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = CourseService.class)
class CourseServiceTest {
    @MockBean
    private CourseService courseService;

    @Test
    void list() {//todo use h2 for docker
        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder().units(5).title("course5").build());
        courses.add(Course.builder().units(2).title("course2").build());
        when(courseService.list()).thenReturn(courses);
        assertArrayEquals(courseService.list().toArray(), courses.toArray());
    }
}