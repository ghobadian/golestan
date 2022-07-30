package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.CourseSection;

import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {

    @Query("SELECT cs FROM CourseSection cs WHERE cs.term.id  = ?1")
    List<CourseSection> findByTerm(Long term_id);

    @Query("SELECT cs FROM CourseSection cs WHERE cs.instructor.id  = ?1")
    List<CourseSection> findByInstructor(Long instructorId);


    @Query("SELECT cs FROM CourseSection cs WHERE cs.course.id  = ?1")
    List<CourseSection> findByCourse(Long courseId);

}
