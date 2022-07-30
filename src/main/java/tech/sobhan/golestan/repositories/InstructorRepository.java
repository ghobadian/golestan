package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.users.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
