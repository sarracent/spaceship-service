package w2m.travel.spaceshipsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import w2m.travel.spaceshipsservice.model.Spaceship;

import java.util.List;

@Repository
public interface SpaceshipRepository extends JpaRepository<Spaceship, Integer> {
    List<Spaceship> findByNameContainingIgnoreCase(String name);
}
