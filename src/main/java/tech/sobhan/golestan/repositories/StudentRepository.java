package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.auth.User;
import tech.sobhan.golestan.models.users.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {


}
