package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.exception.RedisOperationException;
import vn.edu.hcmute.utecare.service.RedisService;
import org.springframework.data.redis.RedisSystemException;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<Object, Object> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Override
    public void set(String key, Object value, long timeout) {
        validateKey(key);
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        } catch (RedisSystemException e) {
            log.error("Failed to set key {} with timeout: {}", key, e.getMessage(), e);
            throw new RedisOperationException("Error setting key with timeout in Redis", e);
        }
    }

    @Override
    public void set(String key, Object value) {
        validateKey(key);
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (RedisSystemException e) {
            log.error("Failed to set key {}: {}", key, e.getMessage(), e);
            throw new RedisOperationException("Error setting key in Redis", e);
        }
    }

    @Override
    public Boolean delete(String key) {
        if (isInvalidKey(key)) {
            return false;
        }
        try {
            return redisTemplate.delete(key);
        } catch (RedisSystemException e) {
            log.error("Failed to delete key {}: {}", key, e.getMessage(), e);
            throw new RedisOperationException("Error deleting key from Redis", e);
        }
    }

    @Override
    public Boolean hasKey(String key) {
        if (isInvalidKey(key)) {
            return false;
        }
        try {
            return redisTemplate.hasKey(key);
        } catch (RedisSystemException e) {
            log.error("Failed to check key {}: {}", key, e.getMessage(), e);
            throw new RedisOperationException("Error checking key existence in Redis", e);
        }
    }

    @Override
    public Boolean expire(String key, long timeout) {
        if (isInvalidKey(key)) {
            return false;
        }
        try {
           return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        } catch (RedisSystemException e) {
            log.error("Failed to set expiration for key {}: {}", key, e.getMessage(), e);
            throw new RedisOperationException("Error setting expiration for key in Redis", e);
        }
    }

    @Override
    public Object get(String key) {
        if (isInvalidKey(key)) {
            return null;
        }
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (RedisSystemException e) {
            log.error("Failed to get key {}: {}", key, e.getMessage(), e);
            throw new RedisOperationException("Error retrieving key from Redis", e);
        }
    }

    @Override
    public Long increment(String key) {
        validateKey(key);
        try {
            return redisTemplate.opsForValue().increment(key);
        } catch (RedisSystemException e) {
            log.error("Failed to increment key {}: {}", key, e.getMessage(), e);
            throw new RedisOperationException("Error incrementing key in Redis", e);
        }
    }

    private void validateKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
    }

    private boolean isInvalidKey(String key) {
        return key == null || key.isEmpty();
    }
}
