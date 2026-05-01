package seekreal.comment.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(0);
        poolConfig.setMaxWait(Duration.ofMillis(-1));
        return poolConfig;
    }
    /**
     * 配置 Redis 连接工厂（使用 Jedis）
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        // 单机模式配置
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName("127.0.0.1"); // 可从配置文件注入 @Value
        redisConfig.setPort(6380);
         redisConfig.setPassword(RedisPassword.of("123456")); // 有密码时开启
        redisConfig.setDatabase(0);

        // Jedis 客户端配置
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .connectTimeout(Duration.ofMillis(3000))
                .usePooling()
                .poolConfig(jedisPoolConfig)
                .build();

        return new JedisConnectionFactory(redisConfig, clientConfig);
    }
}
