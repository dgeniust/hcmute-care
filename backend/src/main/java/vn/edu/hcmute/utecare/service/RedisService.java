package vn.edu.hcmute.utecare.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    void set(String key, Object value, long timeout, TimeUnit unit);
    void set(String key, Object value);
    Boolean delete(String key);
    Boolean hasKey(String key);
    Boolean expire(String key, long timeout, TimeUnit unit);
    Long getExpire(String key, TimeUnit unit);
    Object get(String key);
    Long increment(String key);
}
