package w2m.travel.spaceshipsservice.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import w2m.travel.spaceshipsservice.dao.SpaceshipJpaRepository;
import w2m.travel.spaceshipsservice.bussines.SpaceshipBO;
import w2m.travel.spaceshipsservice.dao.SerieRepository;
import w2m.travel.spaceshipsservice.exception.impl.ResourceNotFoundException;
import w2m.travel.spaceshipsservice.model.Serie;
import w2m.travel.spaceshipsservice.model.Spaceship;
import w2m.travel.spaceshipsservice.service.KafkaMessagePublisherService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SpaceshipServiceImplTest {

    @Mock
    private SpaceshipJpaRepository spaceshipJpaRepository;

    @Mock
    private SerieRepository serieRepository;

    @Mock
    private SpaceshipBO spaceshipBO;

    @Mock
    private KafkaMessagePublisherService kafkaMessagePublisherService;

    @InjectMocks
    private SpaceshipServiceImpl spaceshipService;

    @Test
    void getAllSpaceships_ShouldReturnPage_WhenSpaceshipsExist() {
        // Arrange
        Page<Spaceship> expectedPage = new PageImpl<>(List.of(new Spaceship()));
        PageRequest pageable = PageRequest.of(0, 10);
        when(spaceshipJpaRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Spaceship> result = spaceshipService.getAllSpaceships(pageable);

        // Assert
        assertEquals(expectedPage, result);
        verify(spaceshipJpaRepository).findAll(pageable);
    }

    @Test
    void getAllSpaceships_ShouldThrowException_WhenNoSpaceshipsFound() {
        // Arrange
        Page<Spaceship> emptyPage = Page.empty();
        PageRequest pageable = PageRequest.of(0, 10);
        when(spaceshipJpaRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            spaceshipService.getAllSpaceships(pageable);
        });
    }

    @Test
    void getSpaceshipById_ShouldReturnSpaceship_WhenFound() {
        // Arrange
        Spaceship expectedSpaceship = new Spaceship();
        int id = 1;
        when(spaceshipBO.getSpaceship(id)).thenReturn(expectedSpaceship);

        // Act
        Spaceship result = spaceshipService.getSpaceshipById(id);

        // Assert
        assertEquals(expectedSpaceship, result);
        verify(spaceshipBO).getSpaceship(id);
    }

    @Test
    void getSpaceshipsByNameContaining_ShouldReturnList_WhenFound() {
        // Arrange
        String name = "Enterprise";
        List<Spaceship> expectedList = List.of(new Spaceship());
        when(spaceshipJpaRepository.findByNameContainingIgnoreCase(name)).thenReturn(expectedList);

        // Act
        List<Spaceship> result = spaceshipService.getSpaceshipsByNameContaining(name);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(expectedList, result);
    }

    @Test
    void getSpaceshipsByNameContaining_ShouldThrowException_WhenNoneFound() {
        // Arrange
        String name = "NonExistentName";
        when(spaceshipJpaRepository.findByNameContainingIgnoreCase(name)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            spaceshipService.getSpaceshipsByNameContaining(name);
        });
    }

    @Test
    void createSpaceship_ShouldReturnCreatedSpaceship_WhenSuccessful() {
        // Arrange
        Spaceship newSpaceship = new Spaceship();
        when(spaceshipJpaRepository.save(any(Spaceship.class))).thenReturn(newSpaceship);
        doNothing().when(kafkaMessagePublisherService).sendMessageToTopic(anyString());

        // Act
        Spaceship result = spaceshipService.createSpaceship(newSpaceship);

        // Assert
        assertEquals(newSpaceship, result);
        verify(spaceshipJpaRepository).save(newSpaceship);
        verify(kafkaMessagePublisherService).sendMessageToTopic(anyString());
    }

    @Test
    void deleteSpaceship_ShouldInvokeDeletion_WhenSpaceshipExists() {
        // Arrange
        int id = 1;
        when(spaceshipJpaRepository.existsById(id)).thenReturn(true);
        doNothing().when(spaceshipJpaRepository).deleteById(id);

        // Act
        spaceshipService.deleteSpaceship(id);

        // Assert
        verify(spaceshipJpaRepository).deleteById(id);
    }

    @Test
    void deleteSpaceship_ShouldThrowException_WhenSpaceshipDoesNotExist() {
        // Arrange
        int id = 1;
        when(spaceshipJpaRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> spaceshipService.deleteSpaceship(id));
    }

    @Test
    void updateSpaceship_ShouldReturnUpdatedSpaceship_WhenFound() {
        // Arrange
        int id = 1;
        Spaceship existingSpaceship = new Spaceship();
        Spaceship updates = new Spaceship();
        when(spaceshipJpaRepository.findById(id)).thenReturn(Optional.of(existingSpaceship));
        when(spaceshipJpaRepository.save(any(Spaceship.class))).thenReturn(updates);

        // Act
        Spaceship result = spaceshipService.updateSpaceship(id, updates);

        // Assert
        assertEquals(updates, result);
        verify(spaceshipJpaRepository).save(existingSpaceship);
    }

    @Test
    void updateSpaceship_ShouldThrowException_WhenNotFound() {
        // Arrange
        int id = 1;
        Spaceship updates = new Spaceship();
        when(spaceshipJpaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> spaceshipService.updateSpaceship(id, updates));
    }

    @Test
    void createSerieForSpaceship_ShouldReturnCreatedSerie_WhenSpaceshipExists() {
        // Arrange
        int spaceshipId = 1;
        Serie newSerie = new Serie();
        Spaceship existingSpaceship = new Spaceship();
        when(spaceshipBO.getSpaceship(spaceshipId)).thenReturn(existingSpaceship);
        when(serieRepository.save(any(Serie.class))).thenReturn(newSerie);

        // Act
        Serie result = spaceshipService.createSerieForSpaceship(spaceshipId, newSerie);

        // Assert
        assertEquals(newSerie, result);
        verify(serieRepository).save(newSerie);
        assertEquals(existingSpaceship, newSerie.getSpaceship());
    }
}