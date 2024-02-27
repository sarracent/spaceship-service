package w2m.travel.spaceshipsservice.bussines.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import w2m.travel.spaceshipsservice.annotations.log.LogBO;
import w2m.travel.spaceshipsservice.bussines.SpaceshipBO;
import w2m.travel.spaceshipsservice.dao.SpaceshipCacheRepository;
import w2m.travel.spaceshipsservice.exception.impl.ResourceNotFoundException;
import w2m.travel.spaceshipsservice.model.Spaceship;
import w2m.travel.spaceshipsservice.dao.SpaceshipJpaRepository;
import w2m.travel.spaceshipsservice.model.cache.CacheSpaceship;

import java.util.Optional;

import static w2m.travel.spaceshipsservice.constants.Constants.UNDERSCORE;
import static w2m.travel.spaceshipsservice.constants.Errors.ERROR_DATABASE_SPACESHIP_NOT_FOUND;

@Component
@AllArgsConstructor
public class SpaceshipBOImpl implements SpaceshipBO {

    private final SpaceshipCacheRepository spaceshipCacheRepository;
    private final SpaceshipJpaRepository spaceshipJpaRepository;

    @Override
    @LogBO
    public Spaceship getSpaceship(int id) {
        String redisKey = getKey(String.valueOf(id));
       //busco en la cache
        Optional<CacheSpaceship> spaceshipCache = spaceshipCacheRepository.findById(redisKey);
       //si existe devuelvo el obj
        if(spaceshipCache.isPresent()) {
            return spaceshipCache.get().getSpaceship();
        }
        Optional<Spaceship> spaceship = spaceshipJpaRepository.findById(id);
        if (spaceship.isEmpty()) {
            throw new ResourceNotFoundException(
                    ERROR_DATABASE_SPACESHIP_NOT_FOUND.getCode(),
                    String.format(ERROR_DATABASE_SPACESHIP_NOT_FOUND.getMessage(), id));
        }
        spaceshipCacheRepository.save(new CacheSpaceship(redisKey, spaceship.get()));
        return spaceship.get();
   }

    private String getKey(String id) {
        return id.concat(UNDERSCORE + "keyRedis");
    }
}
