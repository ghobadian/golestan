package tech.sobhan.golestan.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.users.Instructor;

@Repository
public interface InstructorRepository extends PagingAndSortingRepository<Instructor, Long> {
}
