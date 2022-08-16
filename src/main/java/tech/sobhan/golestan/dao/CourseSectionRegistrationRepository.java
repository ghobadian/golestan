package tech.sobhan.golestan.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.CourseSectionRegistration;
import tech.sobhan.golestan.models.users.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSectionRegistrationRepository extends PagingAndSortingRepository<CourseSectionRegistration, Long> {
    Optional<CourseSectionRegistration> findByCourseSectionIdAndStudentId(Long courseSection_id, Long student_id);

    boolean existsByCourseSectionIdAndStudentId(Long courseSection_id, Long student_id);

    List<CourseSectionRegistration> findByStudent(Student student);

    int countByCourseSectionId(Long courseSection_id);

    List<CourseSectionRegistration> findByCourseSectionId(Long courseSectionId);
}
