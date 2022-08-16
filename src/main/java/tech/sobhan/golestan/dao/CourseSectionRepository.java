package tech.sobhan.golestan.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.Term;

import java.util.List;

@Repository
public interface CourseSectionRepository extends PagingAndSortingRepository<CourseSection, Long> {

    List<CourseSection> findByTerm(Term term);

    boolean existsByIdAndTerm(Long id, Term term);

    List<CourseSection> findAllByTermAndInstructor_User_NameAndCourse_Title(Term term,
                                                                            String instructor_user_name,
                                                                            String course_title,
                                                                            PageRequest pageRequest);

    List<CourseSection> findByInstructorId(Long instructorId);
}
