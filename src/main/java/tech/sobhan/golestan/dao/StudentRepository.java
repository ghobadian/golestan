package tech.sobhan.golestan.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.users.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student,Long> {
    Optional<Student> findByUserUsername(String user_username);
}
