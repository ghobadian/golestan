package tech.sobhan.golestan.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.users.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
}
