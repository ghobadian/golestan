package tech.sobhan.golestan.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;

import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {

    List<CourseSection> findByTerm(Term term);

    List<CourseSection> findByInstructor(Instructor instructor);

    List<CourseSection> findByCourse_Title(String courseTitle);

    List<CourseSection> findByCourse_TitleAndInstructorUserName(String course_title, String instructor_user_name);
}
