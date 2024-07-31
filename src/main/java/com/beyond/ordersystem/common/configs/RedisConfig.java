package com.beyond.ordersystem.common.configs;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}") // application.yml파일의 spring > redis > host의 정보를 소스코드의 변수로 가져오는 것
    public String host;

    @Value("${spring.redis.port}") // application.yml파일의 spring > redis > port의 정보를 소스코드의 변수로 가져오는 것
    public int port;

    @Bean
    @Qualifier("2") // default : 1
    // RedisConnectionFactory는 Redis서버와의 연결을 설정하는 역할 (정보성 요소)
    // LettuceConnectionFactory는 RedisConnectionFactory의 구현체로서 실질적인 역할 수행
    public RedisConnectionFactory redisConnectionFactory(){
//        return new LettuceConnectionFactory(host, port);

        // 유연하게 사용
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(1); // 1번 DB 사용
//        configuration.setPassword("1234");
        return new LettuceConnectionFactory(configuration);
    }

    // redisTemplate은 redis와 상호작용할 때 redis key,value의 형식을 정의 (실질적으로 데이터를 넣는 곳)
    @Bean
    @Qualifier("2")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("2") RedisConnectionFactory redisConnectionFactory){
        // Object : 보통 json형태의 데이터가 들어올 것
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // String 형태를 직렬화 시키겠다. (String으로 직렬화)
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); //json으로 직렬화
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @Qualifier("3") // default : 1
    // RedisConnectionFactory는 Redis서버와의 연결을 설정하는 역할 (정보성 요소)
    // LettuceConnectionFactory는 RedisConnectionFactory의 구현체로서 실질적인 역할 수행
    public RedisConnectionFactory stockFactory(){
//        return new LettuceConnectionFactory(host, port);

        // 유연하게 사용
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(2); // 2번 DB 사용
//        configuration.setPassword("1234");
        return new LettuceConnectionFactory(configuration);
    }

    // redisTemplate은 redis와 상호작용할 때 redis key,value의 형식을 정의 (실질적으로 데이터를 넣는 곳)
    @Bean
    @Qualifier("3")
    public RedisTemplate<String, Object> stockRedisTemplate(@Qualifier("3") RedisConnectionFactory redisConnectionFactory){
        // Object : 보통 json형태의 데이터가 들어올 것
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // String 형태를 직렬화 시키겠다. (String으로 직렬화)
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); //json으로 직렬화
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }



    // redisTemplate.opsForValue().set(key, value)
    // redisTemplate.opsForValue().get(key)
    // redisTemplate.opsForValue().increment 또는 decrement
}
