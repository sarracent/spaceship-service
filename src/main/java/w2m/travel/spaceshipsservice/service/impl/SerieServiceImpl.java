package w2m.travel.spaceshipsservice.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import w2m.travel.spaceshipsservice.annotations.log.LogService;
import w2m.travel.spaceshipsservice.exception.impl.ResourceNotFoundException;
import w2m.travel.spaceshipsservice.model.Serie;
import w2m.travel.spaceshipsservice.repository.SerieRepository;
import w2m.travel.spaceshipsservice.service.SerieService;
import w2m.travel.spaceshipsservice.utility.Util;

import java.util.List;

import static w2m.travel.spaceshipsservice.constants.Errors.ERROR_DATABASE_SERIESS_NOT_FOUND;

@Service
@AllArgsConstructor
public class SerieServiceImpl implements SerieService {

   private final SerieRepository serieRepository;

    @Override
    public Serie createSerie(Serie serie) {
        return serieRepository.save(serie);
    }

    @Override
    @LogService
    public List<Serie> getAllSeries() {
        List<Serie> series = serieRepository.findAll();
    if (Util.isNullOrEmpty(series)) {
            throw new ResourceNotFoundException(
                    ERROR_DATABASE_SERIESS_NOT_FOUND.getCode(),
                    String.format(ERROR_DATABASE_SERIESS_NOT_FOUND.getMessage()));
        }
        return series;
    }
}
