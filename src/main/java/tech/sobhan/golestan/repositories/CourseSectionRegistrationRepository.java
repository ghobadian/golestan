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
//    @Query("SELECT c FROM CourseSectionRegistration c WHERE c.courseSection.id = ?1 AND c.student.id = ?2")
    Optional<CourseSectionRegistration> findByCourseSectionAndStudent(CourseSection courseSection, Student student);

//    @Query("SELECT c FROM CourseSectionRegistration c WHERE c.student.id = ?1")
    List<CourseSectionRegistration> findByStudent(Student student);//todo remove queries
//    List<CourseSectionRegistration> findByStudentAnd(Student student, Term term);//todo find By two factors that are not relevant

//    @Query("SELECT c FROM CourseSectionRegistration c WHERE c.courseSection.id = ?1")
    List<CourseSectionRegistration> findByCourseSection(CourseSection courseSection);

//    @Query("select count(c) FROM CourseSectionRegistration c where c.courseSection.id = ?1")
//    int countAllByStudent(Student student);
    int countByCourseSection(CourseSection courseSection);
}
