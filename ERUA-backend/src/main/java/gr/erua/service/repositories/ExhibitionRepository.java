package gr.erua.service.repositories;

import gr.erua.service.models.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExhibitionRepository extends JpaRepository<Exhibition,Long> {
    List<Exhibition> findByOrganizerId(UUID Id);
}
