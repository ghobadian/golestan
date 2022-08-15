package tech.sobhan.golestan.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.sobhan.golestan.models.Term;

@Repository
public interface TermRepository extends PagingAndSortingRepository<Term, Long> {
    boolean existsByTitle(String title);
}
