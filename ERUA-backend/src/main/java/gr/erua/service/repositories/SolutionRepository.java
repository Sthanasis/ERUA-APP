package gr.erua.service.repositories;

import gr.erua.service.models.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution,Long> {

    Page<Solution> findByTypeOfSolutionIgnoreCase(String typeOfSolution, Pageable pageable);

    Page<Solution> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Solution> findByNameContainingIgnoreCase(String name);

    Page<Solution> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);
    List<Solution> findByDescriptionContainingIgnoreCase(String description);

    @Query("SELECT DISTINCT s.typeOfSolution FROM Solution s")
    List<String> getDistinctTypes();

    List<Solution> findByOwnerEmail(String email);
}
