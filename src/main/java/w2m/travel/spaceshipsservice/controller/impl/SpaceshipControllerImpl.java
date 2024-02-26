package w2m.travel.spaceshipsservice.controller.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import w2m.travel.spaceshipsservice.controller.SpaceshipController;
import w2m.travel.spaceshipsservice.model.Serie;
import w2m.travel.spaceshipsservice.model.Spaceship;
import w2m.travel.spaceshipsservice.model.response.*;
import w2m.travel.spaceshipsservice.service.SpaceshipService;

import java.net.URI;

import static w2m.travel.spaceshipsservice.constants.Constants.*;

@RestController
@AllArgsConstructor
public class SpaceshipControllerImpl implements SpaceshipController {
    private final SpaceshipService spaceshipService;

    @Override
    public ResponseEntity<SpaceshipsResponse> getAllSpaceships(
            @PageableDefault(page = 0, size = 20) Pageable pageable) {
        var response = SpaceshipsResponse.builder()
                .spaceships(spaceshipService.getAllSpaceships(pageable))
                .resultCode(OK_VALUE)
                .resultMessage(OK_MSG)
                .build();

        return ResponseEntity.ok()
                .body(response);
    }

    @Override
    public ResponseEntity<SpaceshipResponse> getSpaceshipById(int id) {
        var response = SpaceshipResponse.builder()
                .spaceship(spaceshipService.getSpaceshipById(id))
                .resultCode(OK_VALUE)
                .resultMessage(OK_MSG)
                .build();

        return ResponseEntity.ok()
                .body(response);
    }

    @Override
    public ResponseEntity<?> deleteSpaceship(int id) {
        spaceshipService.deleteSpaceship(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<SpaceshipsByNameResponse> getAllSpaceships(String name) {
        var response = SpaceshipsByNameResponse.builder()
                .spaceships(spaceshipService.getSpaceshipsByNameContaining(name))
                .resultCode(OK_VALUE)
                .resultMessage(OK_MSG)
                .build();

        return ResponseEntity.ok()
                .body(response);
    }

    @Override
    public ResponseEntity<SpaceshipResponse> createSpaceship(Spaceship spaceship) {
        var createdSpaceship = spaceshipService.createSpaceship(spaceship);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSpaceship.getId())
                .toUri();
        var response = SpaceshipResponse.builder()
                .spaceship(createdSpaceship)
                .resultCode(CREATED_VALUE)
                .resultMessage(CREATED_MSG)
                .build();

        return ResponseEntity.created(location)
                .body(response);
    }

    @Override
    public ResponseEntity<SpaceshipResponse> updateSpaceship(int id, Spaceship spaceshipDetails) {
        var response = SpaceshipResponse.builder()
                .spaceship(spaceshipService.updateSpaceship(id, spaceshipDetails))
                .resultCode(OK_VALUE)
                .resultMessage(OK_MSG)
                .build();

        return ResponseEntity.ok()
                .body(response);
    }

    @Override
    public ResponseEntity<SeriesResponse> getSeriesForSpaceship(int id) {
        var spaceship = spaceshipService.getSpaceshipById(id);
        var response = SeriesResponse.builder()
                .series(spaceship.getSeries())
                .resultCode(OK_VALUE)
                .resultMessage(OK_MSG)
                .build();

        return ResponseEntity.ok()
                .body(response);
    }

    @Override
    public ResponseEntity<SerieResponse> createSerieForSpaceship(int id, Serie serie) {
        var savedSerie = spaceshipService.createSerieForSpaceship(id, serie);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedSerie.getId())
                .toUri();
        var response = SerieResponse.builder()
                .serie(savedSerie)
                .resultCode(CREATED_VALUE)
                .resultMessage(CREATED_MSG)
                .build();

        return ResponseEntity.created(location)
                .body(response);
    }

}
