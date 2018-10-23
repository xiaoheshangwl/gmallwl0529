package com.atguigu.gmall.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/*
jedis 连接池的配置
 */
@Configuration
public class GmallRedisConfig {
    //暴露JedisPool;方便获取原生的jedis客户端
    //JedisConnectionFactory是redis自动配置已经做好的。我们直接注入使用
//以后使用jedis客户端只需要自动注入jedisPool，然后jedisPool.getResource()
    @Bean
    public JedisPool jedisPoolConfig(JedisConnectionFactory factory) {
        JedisPoolConfig config = factory.getPoolConfig();
        JedisPool jedisPool = new JedisPool(config, factory.getHostName(), factory.getPort(), factory.getTimeout());
        return jedisPool;
    }
}

