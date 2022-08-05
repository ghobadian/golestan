package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.users.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.student.id = ?1")
    Optional<User> findByStudentId(Long studentId);

    @Query("SELECT u FROM User u WHERE u.name = ?1")
    List<User> findByName(String name);

    @Query("SELECT u FROM User u WHERE u.instructor.id = ?1")
    Optional<User> findByInstructorId(Long id);

    @Query("SELECT u FROM User u WHERE u.phone = ?1")
    Optional<User> findByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.nationalId = ?1")
    Optional<User> findByNationalId(String nationalId);

    @Query("SELECT u FROM User u WHERE u.admin = true")
    List<User> findByAdmin();
}
