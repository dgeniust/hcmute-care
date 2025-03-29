package vn.edu.hcmute.utecare.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.utecare.exception.RedisOperationException;
import vn.edu.hcmute.utecare.service.RedisService;
import org.springframework.data.redis.RedisSystemException;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        validateKey(key);
        if (unit == null) {
            throw new IllegalArgumentException("TimeUnit cannot be null");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("Timeout cannot be negative");
        }
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
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
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        validateKey(key);
        if (unit == null) {
            throw new IllegalArgumentException("TimeUnit cannot be null");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("Timeout cannot be negative");
        }
        try {
            return redisTemplate.expire(key, timeout, unit);
        } catch (RedisSystemException e) {
            log.error("Failed to set expiration for key {}: {}", key, e.getMessage(), e);
            throw new RedisOperationException("Error setting expiration for key in Redis", e);
        }
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        validateKey(key);
        try {
            return redisTemplate.getExpire(key, timeUnit);
        } catch (RedisSystemException e) {
            log.error("Failed to get expiration for key {}: {}", key, e.getMessage(), e);
            throw new RedisOperationException("Error getting expiration for key from Redis", e);
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
        if (isInvalidKey(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
    }

    private boolean isInvalidKey(String key) {
        return key == null || key.isEmpty();
    }
}