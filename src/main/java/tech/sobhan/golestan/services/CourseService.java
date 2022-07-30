package tech.sobhan.golestan.services;

import org.springframework.stereotype.Service;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.business.exceptions.CourseNotFoundException;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.repositories.CourseRepository;

import java.util.List;

import static tech.sobhan.golestan.utils.Util.createLog;
import static tech.sobhan.golestan.utils.Util.deleteLog;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> list() {
        List<Course> allCourses = courseRepository.findAll();
//        if(allCourses.isEmpty()){
//            throw new CourseNotFoundException();
//        }
        return allCourses;
    }

    public Course create(int units, String title) {
        Course course = Course.builder().units(units).title(title).build();
        return create(course);
    }

    public Course create(Course course) {
        if (courseExists(list(), course)) return null;
        createLog(Course.class, course.getId());
        return courseRepository.save(course);
    }

    private boolean courseExists(List<Course> allCourses, Course course) {
        for (Course c : allCourses) {
            if(course.equals(c)){
                System.out.println("ERROR403 duplicate Courses");
                return true;
            }
        }
        return false;
    }

    public Course read(Long id) {
        return courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
    }

    public void update(Course newCourse, Long id) {
        courseRepository.findById(id).map(user -> {
            user = newCourse.clone();
            return courseRepository.save(user);//todo sus for saveUser instead of user
        }).orElseGet(() -> {
            newCourse.setId(id);
            return courseRepository.save(newCourse.clone());
        });
    }

    public void delete(Long id) {
        courseRepository.delete(read(id));
        deleteLog(Course.class, id);
    }


}
