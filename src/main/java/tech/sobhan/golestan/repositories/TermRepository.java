package tech.sobhan.golestan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.Term;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

//    Term findByUserName(String username);
}
