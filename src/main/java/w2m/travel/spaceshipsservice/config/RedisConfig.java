package w2m.travel.spaceshipsservice.config;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.time.Duration;

/**
 * Configuración de conexion a Redis en cluster
 * MaxIdle y Maxtotal que tenga el mismo valor
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Data
public class RedisConfig {

    private String host;
    private int port;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private int maxWaitMillis;

    /**
     * Configuracion con pool
     * @return
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        LettucePoolingClientConfiguration lettuceConfigClient = LettucePoolingClientConfiguration
                .builder()
                .poolConfig(buildPoolConfig())
                .build();

        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port), lettuceConfigClient);
    }

    public GenericObjectPoolConfig buildPoolConfig(){
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWait(Duration.ofMillis(maxWaitMillis));
        return poolConfig;

    }


}
