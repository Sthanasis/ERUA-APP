package gr.erua.service.repositories;

import gr.erua.service.models.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StakeholderRepository extends JpaRepository<Stakeholder, UUID> {

    Optional<Stakeholder> findByEmail(String email);

    List<Stakeholder> findAllByAuthorized(Boolean authorized);

}
