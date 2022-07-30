package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.sobhan.golestan.models.CourseSectionRegistration;

import java.util.List;
import java.util.Optional;

public interface CourseSectionRegistrationRepository extends JpaRepository<CourseSectionRegistration, Long> {
    @Query("SELECT c FROM CourseSectionRegistration c WHERE c.courseSection.id = ?1 AND c.student.studentId = ?2")//todo test later
    Optional<CourseSectionRegistration> findByCourseSectionAndStudent(Long courseSectionId, Long studentId);

    @Query("SELECT c FROM CourseSectionRegistration c WHERE c.student.studentId = ?1")//todo test later
    List<CourseSectionRegistration> findByStudent(Long studentId);

    @Query("SELECT c FROM CourseSectionRegistration c WHERE c.courseSection.id = ?1")
    List<CourseSectionRegistration> findByCourseSection(Long courseSectionId);
}
