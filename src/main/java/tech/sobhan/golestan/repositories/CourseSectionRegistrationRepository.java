package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.CourseSection;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSectionRegistrationRepository extends JpaRepository<CourseSectionRegistration, Long> {
    Optional<CourseSectionRegistration> findByCourseSectionAndStudent(CourseSection courseSection, Student student);

    List<CourseSectionRegistration> findByStudent(Student student);//todo remove queries

    List<CourseSectionRegistration> findByCourseSection(CourseSection courseSection);

    int countByCourseSection(CourseSection courseSection);
}
