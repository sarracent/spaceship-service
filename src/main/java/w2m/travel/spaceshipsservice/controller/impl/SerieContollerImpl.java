package w2m.travel.spaceshipsservice.controller.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import w2m.travel.spaceshipsservice.controller.SerieController;
import w2m.travel.spaceshipsservice.model.Serie;
import w2m.travel.spaceshipsservice.model.response.SerieResponse;
import w2m.travel.spaceshipsservice.model.response.SeriesResponse;
import w2m.travel.spaceshipsservice.service.SerieService;

import java.net.URI;

import static w2m.travel.spaceshipsservice.constants.Constants.*;
import static w2m.travel.spaceshipsservice.constants.Constants.OK_MSG;

@RestController
@AllArgsConstructor
public class SerieContollerImpl implements SerieController {
    private final SerieService serieService;

    @Override
    public ResponseEntity<SerieResponse> createSerie(Serie serie) {
        Serie createdSerie = serieService.createSerie(serie);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSerie.getId())
                .toUri();
        var response = SerieResponse.builder()
                .serie(createdSerie)
                .resultCode(CREATED_VALUE)
                .resultMessage(CREATED_MSG)
                .build();

        return ResponseEntity.created(location)
                .body(response);
    }

    @Override
    public ResponseEntity<SeriesResponse> getAllSeries() {
        var response = SeriesResponse.builder()
                .series(serieService.getAllSeries())
                .resultCode(OK_VALUE)
                .resultMessage(OK_MSG)
                .build();

        return ResponseEntity.ok()
                .body(response);
    }
}
