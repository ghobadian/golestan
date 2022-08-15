package tech.sobhan.golestan.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSectionRegistrationRepository extends PagingAndSortingRepository<CourseSectionRegistration, Long> {
    Optional<CourseSectionRegistration> findByCourseSectionAndStudent(CourseSection courseSection, Student student);

    boolean existsByCourseSectionAndStudent(CourseSection courseSection, Student student);

    List<CourseSectionRegistration> findByStudent(Student student);

    List<CourseSectionRegistration> findByCourseSection(CourseSection courseSection);

    int countByCourseSection(CourseSection courseSection);
}
