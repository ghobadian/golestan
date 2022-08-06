package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.Term;

import java.util.Optional;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {
//    @Query("SELECT term FROM Term term Where term.title =?1")
    Optional<Term> findByTitle(String title);

//    Term findByUserName(String username);
}
