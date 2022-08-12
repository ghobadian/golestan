package tech.sobhan.golestan.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.Term;

import java.util.Optional;

@Repository
public interface TermRepository extends PagingAndSortingRepository<Term, Long> {
    Optional<Term> findByTitle(String title);

}
