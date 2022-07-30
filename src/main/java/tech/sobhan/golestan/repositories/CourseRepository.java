package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
