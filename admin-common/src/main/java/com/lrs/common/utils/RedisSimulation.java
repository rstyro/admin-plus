package com.lrs.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 小项目懒得集成Redis，可以模拟一下，有条件的直接使用 Redis 即可
 * Redis模拟
 * @author lrs
 */
@Slf4j
public class RedisSimulation {

    private Map<String, Object> dataMap;
    private Map<String, Long> expirationMap;

    private ScheduledExecutorService executorService;

    public RedisSimulation() {
        dataMap = new WeakHashMap<>();
        expirationMap = new ConcurrentHashMap<>();
        executorService = Executors.newSingleThreadScheduledExecutor();
        scheduleExpirationCheck();
    }


    public <T> void set(String key, T value, long expiration) {
       set(key,value,expiration,TimeUnit.MILLISECONDS);
    }

    /**
     * 设置缓存
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param expiration 过期时间
     * @param unit 时间单位
     */
    public <T> void set(String key, T value, long expiration, TimeUnit unit) {
        dataMap.put(key, value);
        expirationMap.put(key, System.currentTimeMillis() + unit.toMillis(expiration));
    }

    /**
     * 如果不存在则设置 并返回 true 如果存在则返回 false
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param expiration 过期时间
     * @return set成功或失败
     */
    public <T> boolean setObjectIfAbsent(String key, T value, long expiration) {
        long currentExpiration = System.currentTimeMillis() + expiration;
        Long oldExpiration = expirationMap.putIfAbsent(key, currentExpiration);
        if (oldExpiration == null || System.currentTimeMillis() > oldExpiration) {
            dataMap.put(key, value);
            expirationMap.put(key, currentExpiration);
            return true;
        }
        return false;
    }

    /**
     * 尝试获取分布式锁（带时间单位）
     * @param lockKey    锁的 key
     * @param requestId  请求标识（用于释放时校验身份）
     * @param expireTime 过期时间
     * @param timeUnit   时间单位
     * @return 是否成功获取锁
     */
    public boolean tryLock(String lockKey, String requestId, long expireTime, TimeUnit timeUnit) {
        long expireMillis = timeUnit.toMillis(expireTime);
        return setObjectIfAbsent(lockKey, requestId, expireMillis);
    }

    /**
     * 尝试获取分布式锁
     * @param lockKey    锁的 key
     * @param requestId  请求标识（值）
     * @param expireTime 过期时间（毫秒）
     * @return 是否成功获取锁
     */
    public boolean tryLock(String lockKey, String requestId, long expireTime) {
        return tryLock(lockKey,requestId,expireTime,TimeUnit.MILLISECONDS);
    }

    /**
     * 释放分布式锁（需要验证 requestId）
     * @param lockKey   锁的 key
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String requestId) {
        String current = get(lockKey);
        if (requestId.equals(current)) {
            del(lockKey);
            return true;
        }
        return false;
    }

    /**
     * 设置有效时间
     * @param key 缓存的键值
     * @param expiration 过期时间
     * @param unit 时间单位
     * @return set成功或失败
     */
    public boolean expire(String key, long expiration, TimeUnit unit) {
        long currentExpiration = System.currentTimeMillis() + unit.toMillis(expiration);
        Long oldExpiration = expirationMap.put(key, currentExpiration);
        if (oldExpiration != null && System.currentTimeMillis() < oldExpiration) {
            return true;
        } else {
            del(key);
            return false;
        }
    }

    public boolean expire(String key, long expiration) {
        return expire(key,expiration,TimeUnit.MILLISECONDS);
    }

    public void del(String key) {
        dataMap.remove(key);
        expirationMap.remove(key);
    }

    /**
     * 获取缓存
     * @param key 缓存的键值
     * @return 缓存的值
     */
    public <T> T get(String key) {
        if (expirationMap.containsKey(key)) {
            long expiration = expirationMap.get(key);
            if (System.currentTimeMillis() < expiration) {
                return (T) dataMap.get(key);
            } else {
                // 过期时间已到，删除该键值对
                del(key);
            }
        }
        return null;
    }

    /**
     * 原子性地将 key 对应的整数值增加 delta（delta 可为负数，实现自减）
     * @param key   缓存的键值
     * @param delta 增加的值（负数则减少）
     * @return 增加后的新值
     * @throws IllegalArgumentException 如果 key 对应的值不是数字类型
     */
    public long incr(String key, long delta) {
        synchronized (dataMap) {
            // 检查过期情况
            Long expiration = expirationMap.get(key);
            if (expiration == null || System.currentTimeMillis() > expiration) {
                // 键不存在或已过期：清理并初始化为 delta
                del(key);
                dataMap.put(key, delta);
                return delta;
            } else {
                // 键存在且未过期，获取当前值
                Object currentObj = dataMap.get(key);
                long currentValue;
                if (currentObj instanceof Number) {
                    currentValue = ((Number) currentObj).longValue();
                } else {
                    throw new IllegalArgumentException("Value for key '" + key + "' is not a number");
                }
                long newValue = currentValue + delta;
                dataMap.put(key, newValue);
                return newValue;
            }
        }
    }

    /**
     * 遍历数据，过期就删除
     */
    private void checkExpiredKeys() {
        Iterator<Map.Entry<String, Long>> iterator = expirationMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            String key = entry.getKey();
            long expiration = entry.getValue();
            if (System.currentTimeMillis() > expiration) {
                // 键已过期，从dataMap和expirationMap中删除
                dataMap.remove(key);
                iterator.remove();
            }
        }
    }

    /**
     * 定时器，定时扫描过期的key，每3分钟
     */
    private void scheduleExpirationCheck() {
        executorService.scheduleAtFixedRate(this::checkExpiredKeys, 1, 3, TimeUnit.MINUTES);
    }

}
