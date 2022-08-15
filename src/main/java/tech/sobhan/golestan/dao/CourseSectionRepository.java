package tech.sobhan.golestan.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;
import tech.sobhan.golestan.models.users.Instructor;

import java.util.List;

@Repository
public interface CourseSectionRepository extends PagingAndSortingRepository<CourseSection, Long> {

    List<CourseSection> findByTerm(Term term);

    List<CourseSection> findByInstructor(Instructor instructor);

    List<CourseSection> findByCourse_Title(String courseTitle);

    List<CourseSection> findByCourse_TitleAndInstructorUserName(String course_title, String instructor_user_name);

    List<CourseSection> findByInstructorUserName(String instructor_user_name);

    List<CourseSection> findAllByTermAndCourse_TitleAndInstructorUserName(Term term,
                                                                          String course_title,
                                                                          String instructor_user_name,
                                                                          PageRequest pageRequest);
    //todo اینطوری کار میده؟
    //todo اینجا اگه مثلا یکی از ورودی ها نال باشه اکسپشن پرتاب میکنه؟
}
