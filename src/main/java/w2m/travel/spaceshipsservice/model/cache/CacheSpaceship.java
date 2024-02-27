package w2m.travel.spaceshipsservice.model.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import w2m.travel.spaceshipsservice.constants.Constants;
import w2m.travel.spaceshipsservice.model.Spaceship;

@Data
@AllArgsConstructor
@Getter
@Builder
@RedisHash(value= Constants.PREFIX_CACHE_SPACESHIP, timeToLive = 300)
public class CacheSpaceship {
    @Id
    String key;
    Spaceship spaceship;
}
