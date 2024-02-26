package w2m.travel.spaceshipsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import w2m.travel.spaceshipsservice.model.Serie;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Integer> {
}
