package org.vxwo.springboot.experience.redis.processor;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.vxwo.springboot.experience.redis.entity.LockDurationSession;
import org.vxwo.springboot.experience.redis.render.RedisTemplateRender;

/**
 * Wrap the lock support for Redis
 *
 * @author vxwo-team
 */

public class RedisLockProcessor {
    private static final AtomicLong SAFE_ATOM = new AtomicLong();

    private static final RedisScript<Boolean> OBTAIN_LOCK_SCRIPT = new DefaultRedisScript<>(
            String.join(" ", "local lockClientId = redis.call('GET', KEYS[1])",
                    "if lockClientId == ARGV[1] then",
                    "redis.call('PEXPIRE', KEYS[1], ARGV[2]) return true",
                    "elseif not lockClientId then",
                    "redis.call('SET', KEYS[1], ARGV[1], 'PX', ARGV[2]) return true", "end",
                    "return false"),
            Boolean.class);

    private static final RedisScript<Boolean> DELETE_UNLOCK_SCRIPT =
            new DefaultRedisScript<>(
                    String.join(" ", "local lockClientId = redis.call('GET', KEYS[1])",
                            "if lockClientId == ARGV[1] then",
                            "redis.call('DEL', KEYS[1]) return true", "end", "return false"),
                    Boolean.class);

    private final RedisTemplate<String, String> redisTemplate;

    public RedisLockProcessor(RedisTemplateRender render) {
        redisTemplate = new RedisTemplate<String, String>();
        render.renderStringTemplate(redisTemplate);
    }

    private Boolean obtainLock(String lockKey, String lockValue, long expireAfter) {
        return redisTemplate.execute(OBTAIN_LOCK_SCRIPT, Collections.singletonList(lockKey),
                lockValue, String.valueOf(expireAfter));
    }

    private boolean removeLockKey(String lockKey, String lockValue) {
        return Boolean.TRUE.equals(redisTemplate.execute(DELETE_UNLOCK_SCRIPT,
                Collections.singletonList(lockKey), lockValue));
    }

    /**
     * Enter lock session and own it
     *
     * @param lockKey  the key for lock session
     * @param duration  the lock max duration time
     * @return  session for lock usage, null if failed
     */
    public LockDurationSession lock(String lockKey, Duration duration) {
        return lock(lockKey, duration, null);
    }

    /**
    * Enter lock session and own it, supports waitting
    *
    * @param lockKey  the key for lock session
    * @param duration  the lock max duration time
    * @param waitting  the lock waitting time
    * @return  session for lock usage, null if failed
    */
    public LockDurationSession lock(String lockKey, Duration duration, Duration waitting) {
        String lockValue = UUID.randomUUID().toString() + ":" + SAFE_ATOM.getAndIncrement();
        long expireAfter = duration.toMillis();
        long waitTime = waitting == null ? -1 : waitting.toMillis();

        boolean acquired = false;
        try {
            if (waitTime == -1L) {
                while (!obtainLock(lockKey, lockValue, expireAfter)) {
                    Thread.sleep(100);
                }
                acquired = true;
            } else {
                long expireTime = System.currentTimeMillis() + waitTime;
                while (!(acquired = obtainLock(lockKey, lockValue, expireAfter))
                        && System.currentTimeMillis() < expireTime) {
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        return !acquired ? null : new LockDurationSession(lockKey, lockValue);
    }

    /**
     *  Leave lock session
     *
     * @param session  the session for unlock
     * @return  false if not owned
     */
    public boolean unlock(LockDurationSession session) {
        if (session == null) {
            return false;
        }

        return removeLockKey(session.getLockKey(), session.getLockValue());
    }
}
