package w2m.travel.spaceshipsservice.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import w2m.travel.spaceshipsservice.model.Serie;
import w2m.travel.spaceshipsservice.model.Spaceship;
import w2m.travel.spaceshipsservice.model.response.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequestMapping(path = "/spaceships", produces = {APPLICATION_JSON_VALUE})
public interface SpaceshipController {

    @GetMapping(produces = {APPLICATION_JSON_VALUE})
    ResponseEntity<SpaceshipsResponse> getAllSpaceships(Pageable pageable);

    @GetMapping(value = "/{id}", produces = {APPLICATION_JSON_VALUE})
    ResponseEntity<SpaceshipResponse> getSpaceshipById(@PathVariable int id);

    @DeleteMapping(value = "/{id}", produces = {APPLICATION_JSON_VALUE})
    ResponseEntity<?> deleteSpaceship(@PathVariable int id);

    @GetMapping(value = "/search", produces = {APPLICATION_JSON_VALUE})
    ResponseEntity<SpaceshipsByNameResponse> getAllSpaceships(@RequestParam String name);

    @PostMapping(produces = {APPLICATION_JSON_VALUE})
    ResponseEntity<SpaceshipResponse> createSpaceship(@Valid @RequestBody Spaceship spaceship);

    @PutMapping(value ="/{id}", produces = {APPLICATION_JSON_VALUE})
    ResponseEntity<SpaceshipResponse> updateSpaceship(@PathVariable int id,
                                                      @Valid @RequestBody Spaceship spaceshipDetails);

    @GetMapping(value = "/{id}/series",produces = {APPLICATION_JSON_VALUE})
    ResponseEntity<SeriesResponse> getSeriesForSpaceship(@PathVariable int id);

    @PostMapping(value = "/{id}/series",produces = {APPLICATION_JSON_VALUE})
    ResponseEntity<SerieResponse> createSerieForSpaceship(@PathVariable int id,
                                                          @Valid @RequestBody Serie serie);
}
