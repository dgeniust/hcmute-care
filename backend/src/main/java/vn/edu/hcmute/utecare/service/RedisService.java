package vn.edu.hcmute.utecare.service;

public interface RedisService {
    void set(String key, Object value, long timeout);

    void set(String key, Object value);

    Boolean delete(String key);

    Boolean hasKey(String key);

    Boolean expire(String key, long timeout);

    Object get(String key);

    Long increment(String key);
}
