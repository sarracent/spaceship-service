package w2m.travel.spaceshipsservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import w2m.travel.spaceshipsservice.model.Serie;
import w2m.travel.spaceshipsservice.model.Spaceship;

import java.util.List;

public interface SpaceshipService {
    Page<Spaceship> getAllSpaceships(Pageable pageable);
    Spaceship getSpaceshipById(int id);

    List<Spaceship> getSpaceshipsByNameContaining(String name);

    Spaceship createSpaceship(Spaceship spaceship);

    void deleteSpaceship(int id);

    Spaceship updateSpaceship(int id, Spaceship spaceshipDetails);

    Serie createSerieForSpaceship(int id, Serie serie);
}
