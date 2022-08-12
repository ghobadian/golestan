package tech.sobhan.golestan.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.users.Student;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student,Long> {
}
