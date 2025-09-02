package com.gzc.infrastructure.adapter.repository;

import com.gzc.infrastructure.redis.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.function.Supplier;

public abstract class AbstractRepository {

    private final Logger logger = LoggerFactory.getLogger(AbstractRepository.class);
    @Resource
    private IRedisService redisService;

    protected <T> T getValueFromCacheOrDb(String cacheKey, Supplier<T> dbCallback){
        T value = redisService.getValue(cacheKey);
        if (null != value) {
            return value;
        }

        // 缓存不存再则在数据库中获取
        T dbValue = dbCallback.get();
        if (null == dbValue) return null;
        redisService.setValue(cacheKey, dbValue, 30 * 60 * 1000);
        return dbValue;
    }
}
