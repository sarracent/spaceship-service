package w2m.travel.spaceshipsservice.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import w2m.travel.spaceshipsservice.model.cache.CacheSpaceship;

@Repository
public interface SpaceshipCacheRepository extends CrudRepository<CacheSpaceship, String> {
}
