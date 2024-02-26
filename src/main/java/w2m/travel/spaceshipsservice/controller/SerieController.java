package w2m.travel.spaceshipsservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import w2m.travel.spaceshipsservice.model.Serie;
import w2m.travel.spaceshipsservice.model.response.SerieResponse;
import w2m.travel.spaceshipsservice.model.response.SeriesResponse;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequestMapping(path = "/series", produces = {APPLICATION_JSON_VALUE})
public interface SerieController {

    @PostMapping(produces = {APPLICATION_JSON_VALUE})
    ResponseEntity<SerieResponse> createSerie(@Valid @RequestBody Serie serie);

    @GetMapping(produces = {APPLICATION_JSON_VALUE})
    ResponseEntity<SeriesResponse> getAllSeries();
}
