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

    boolean existsByIdAndTerm(Long id, Term term);

    List<CourseSection> findAllByTermAndInstructor_User_NameAndCourse_Title(Term term,
                                                                            String instructor_user_name,
                                                                            String course_title,
                                                                            PageRequest pageRequest);
}
