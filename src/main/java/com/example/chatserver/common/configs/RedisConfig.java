package com.example.chatserver.common.configs;


import com.example.chatserver.chat.service.RedisPubSubService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    // 연결 기본 객체
    @Bean
    @Qualifier("chatPubSub")
    public RedisConnectionFactory chatPubSubFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        // 이렇게 데이터베이스 지정해서 다양한 연결 기본 객체를 만들어서 용도에 맞게 다른 데이터베이스 쓸 수 있음
        //configuration.setDatabase(1);
        return new LettuceConnectionFactory(configuration);
    }

    // publish 객체
    @Bean
    @Qualifier("chatPubSub")
    // 일반적으로 RedisTemplate<key,value>를 사용
    // 다만 지금은 redis pub/sub을 활용해 메시지를 전파용도로 쓰기 때문에 key value 형식 데이터 저장 용도가 아님
    public StringRedisTemplate chatPubSubTemplate(@Qualifier("chatPubSub") RedisConnectionFactory factory) {

        return new StringRedisTemplate(factory);
    }

    // subscribe 객체
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            @Qualifier("chatPubSub") RedisConnectionFactory factory,
            MessageListenerAdapter listenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

        return container;
    }

    // redis에서 수신된 메시지를 처리하는 객체 생성
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisPubSubService redisPubSubService) {
        //RedisPubSubService의 특정 메서드가 수신된 메시지를 처리하도록 지정
        return new MessageListenerAdapter(redisPubSubService, "onMessage");

    }
}
