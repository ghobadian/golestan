package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.Course;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;

import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {

//    @Query("SELECT cs FROM CourseSection cs WHERE cs.term.id  = ?1")
    List<CourseSection> findByTerm(Term term);

//    @Query("SELECT cs FROM CourseSection cs WHERE cs.instructor.id  = ?1")
    List<CourseSection> findByInstructor(Instructor instructor);


//    @Query("SELECT cs FROM CourseSection cs WHERE cs.course.id  = ?1")
    List<CourseSection> findByCourse(Course course);

//    @Query("SELECT cs FROM CourseSection cs WHERE cs.course.title  = ?1")
    List<CourseSection> findByCourse_Title(String courseTitle);
}
