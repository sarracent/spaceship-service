package w2m.travel.spaceshipsservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import w2m.travel.spaceshipsservice.dao.SerieRepository;
import w2m.travel.spaceshipsservice.exception.impl.ResourceNotFoundException;
import w2m.travel.spaceshipsservice.model.Serie;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static w2m.travel.spaceshipsservice.constants.Errors.ERROR_DATABASE_SERIESS_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
public class SerieServiceImplTest {

    @Mock
    private SerieRepository serieRepository;

    @InjectMocks
    private SerieServiceImpl serieService;

    @Test
    public void createSerie_ShouldReturnCreatedSerie_WhenSuccessful() {
        // Arrange
        Serie newSerie = new Serie();
        when(serieRepository.save(any(Serie.class))).thenReturn(newSerie);

        // Act
        Serie result = serieService.createSerie(newSerie);

        // Assert
        assertEquals(newSerie, result);
        verify(serieRepository).save(newSerie);
    }

    @Test
    public void getAllSeries_ShouldReturnListOfSeries_WhenSuccessful() {
        // Arrange
        List<Serie> expectedSeries = List.of(new Serie(), new Serie());
        when(serieRepository.findAll()).thenReturn(expectedSeries);

        // Act
        List<Serie> result = serieService.getAllSeries();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(expectedSeries.size(), result.size());
        assertEquals(expectedSeries, result);
    }

    @Test
    public void getAllSeries_ShouldThrowResourceNotFoundException_WhenNoSeriesFound() {
        // Arrange
        when(serieRepository.findAll()).thenReturn(List.of());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> serieService.getAllSeries(),
                "Expected getAllSeries() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains(ERROR_DATABASE_SERIESS_NOT_FOUND.getMessage()));
    }
}