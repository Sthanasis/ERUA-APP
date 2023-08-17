package gr.erua.service.repositories;

import gr.erua.service.models.Problem;
import gr.erua.service.models.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem,Long> {
//    @Query("SELECT a FROM Author a JOIN a.phoneNumbers p WHERE p = :keywordList")
    Page<Problem> findByKeywordListIgnoreCase(String keywordList, Pageable pageable);

    Page<Problem> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Problem> findByNameContainingIgnoreCase(String name);

    Page<Problem> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    List<Problem> findByDescriptionContainingIgnoreCase(String description);

    List<Problem> findByOwnerEmail(String email);
}
