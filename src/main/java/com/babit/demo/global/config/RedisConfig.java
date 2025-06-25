package com.babit.demo.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")    //@Value는 application.yml에 있는 환경변수의 값을 읽어옴
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean  //Redis 데이터베이스와의 연결을 생성하는 팩토리
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean  //데이터 조회 및 저장 등 Redis 데이터 조작을 위한 템플릿 클래스
    public RedisTemplate<?, ?> redisTemplate() {
        // redisTemplate를 받아와서 set, get, delete를 사용
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        // setKeySerializer, setValueSerializer 설정
        // -> redis-cli을 통해 직접 데이터를 조회 시 알아볼 수 없는 형태로 출력되는 것을 방지
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        //"커넥션 팩토리"를 RedisTemplate에 등록
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}