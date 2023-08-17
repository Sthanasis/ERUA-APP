package gr.erua.service.repositories;

import gr.erua.service.models.EruaMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EruaMemberRepository extends JpaRepository<EruaMember, UUID> {

    Optional<EruaMember> findByEmail(String email);

}
