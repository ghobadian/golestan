package tech.sobhan.golestan.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.users.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByStudentId(Long studentId);

    Optional<User> findByInstructorId(Long instructor_id);

    List<User> findByAdminTrue();

    boolean existsByInstructorId(Long instructorId);

    boolean existsByPhone(String phone);

    boolean existsByUsername(String username);

    boolean existsByNationalId(String nationalId);
}
