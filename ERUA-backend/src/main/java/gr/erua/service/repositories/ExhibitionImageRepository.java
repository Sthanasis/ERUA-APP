package gr.erua.service.repositories;

import gr.erua.service.models.ExhibitionImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExhibitionImageRepository extends JpaRepository<ExhibitionImage,Long> {
    List<ExhibitionImage> findByOwnerId(UUID Id);

}
