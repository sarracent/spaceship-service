package w2m.travel.spaceshipsservice.service;

import w2m.travel.spaceshipsservice.model.Serie;

import java.util.List;

public interface SerieService {
    Serie createSerie(Serie serie);

    List<Serie> getAllSeries();
}
