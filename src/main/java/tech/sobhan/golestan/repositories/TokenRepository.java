package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    boolean existsByToken(String token);

    Token findByToken(String token);

    boolean existsByUsername(String username);
}
