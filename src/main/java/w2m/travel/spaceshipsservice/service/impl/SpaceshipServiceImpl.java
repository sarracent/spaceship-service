package w2m.travel.spaceshipsservice.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import w2m.travel.spaceshipsservice.annotations.log.LogService;
import w2m.travel.spaceshipsservice.exception.impl.ResourceNotFoundException;
import w2m.travel.spaceshipsservice.model.Serie;
import w2m.travel.spaceshipsservice.model.Spaceship;
import w2m.travel.spaceshipsservice.repository.SerieRepository;
import w2m.travel.spaceshipsservice.repository.SpaceshipRepository;
import w2m.travel.spaceshipsservice.service.SpaceshipService;
import w2m.travel.spaceshipsservice.utility.Util;

import java.util.List;

import static w2m.travel.spaceshipsservice.constants.Errors.*;

@Service
@AllArgsConstructor
public class SpaceshipServiceImpl implements SpaceshipService {

   private final SpaceshipRepository spaceshipRepository;
   private final SerieRepository serieRepository;

   @Override
   @LogService
   public Page<Spaceship> getAllSpaceships(Pageable pageable) {
       Page<Spaceship> spaceshipPage = spaceshipRepository.findAll(pageable);
       if (spaceshipPage.isEmpty()) {
           throw new ResourceNotFoundException(
                   ERROR_DATABASE_SPACESHIPS_NOT_FOUND.getCode(),
                   String.format(ERROR_DATABASE_SPACESHIPS_NOT_FOUND.getMessage()));
       }
       return spaceshipPage;
   }

    @Override
    @LogService
    public Spaceship getSpaceshipById(int id) {
        return spaceshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ERROR_DATABASE_SPACESHIP_NOT_FOUND.getCode(),
                        String.format(ERROR_DATABASE_SPACESHIP_NOT_FOUND.getMessage(), id)));
    }

    @Override
    @LogService
    public List<Spaceship> getSpaceshipsByNameContaining(String name) {
       List<Spaceship> spaceshipList = spaceshipRepository.findByNameContainingIgnoreCase(name);
        if (Util.isNullOrEmpty(spaceshipList))
            throw new ResourceNotFoundException(
                    ERROR_DATABASE_SPACESHIPS_NOT_CONTAINING_NAME.getCode(),
                    String.format(ERROR_DATABASE_SPACESHIPS_NOT_CONTAINING_NAME.getMessage(), name));
        return spaceshipList;
    }

    @Override
    @LogService
    public Spaceship createSpaceship(Spaceship spaceship) {
        return spaceshipRepository.save(spaceship);
    }

    @Override
    @LogService
    public void deleteSpaceship(int id) {
        if (!spaceshipRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    ERROR_DATABASE_SPACESHIP_NOT_FOUND.getCode(),
                    String.format(ERROR_DATABASE_SPACESHIP_NOT_FOUND.getMessage(), id));
        }
        spaceshipRepository.deleteById(id);
    }

    @Override
    @LogService
    public Spaceship updateSpaceship(int id, Spaceship spaceshipDetails) {
        Spaceship spaceship = spaceshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ERROR_DATABASE_SPACESHIP_NOT_FOUND.getCode(),
                        String.format(ERROR_DATABASE_SPACESHIP_NOT_FOUND.getMessage(), id)));

        spaceship.setName(spaceshipDetails.getName());
        spaceship.setModel(spaceshipDetails.getModel());
        spaceship.setColor(spaceshipDetails.getColor());
        spaceship.setManufacturingDate(spaceshipDetails.getManufacturingDate());

        return spaceshipRepository.save(spaceship);
    }

    @Override
    @LogService
    public Serie createSerieForSpaceship(int id, Serie serie) {
        var spaceship = getSpaceshipById(id);
        serie.setSpaceship(spaceship);
        return serieRepository.save(serie);
    }
}
