package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.users.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByStudentId(Long studentId);

    List<User> findByName(String name);

    Optional<User> findByInstructorId(Long instructor_id);

    Optional<User> findByPhone(String phone);

    Optional<User> findByNationalId(String nationalId);
    List<User> findByAdminTrue();
}
