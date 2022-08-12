package tech.sobhan.golestan.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.Course;

@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
    boolean existsByTitle(String title);
}
