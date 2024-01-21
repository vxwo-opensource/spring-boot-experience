package org.vxwo.springboot.experience.redis.processor;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.vxwo.springboot.experience.redis.entity.FrequencyDurationSession;
import org.vxwo.springboot.experience.redis.render.RedisTemplateRender;

/**
 * Wrap the frequency support for Redis
 *
 * @author vxwo-team
 */

public class RedisFrequencyProcessor {
    private static final AtomicLong SAFE_ATOM = new AtomicLong();

    private static final RedisScript<Boolean> UNSAFE_LOCK_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('SET', KEYS[1], ARGV[1], 'NX', 'PX', ARGV[2]) then return true else return false end",
            Boolean.class);

    private static final RedisScript<Boolean> UNSAFE_UNLOCK_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('GET', KEYS[1]) == ARGV[1] then redis.call('DEL', KEYS[1]) return true else return false end",
            Boolean.class);

    private final RedisTemplate<String, String> redisTemplate;

    public RedisFrequencyProcessor(RedisTemplateRender render) {
        redisTemplate = new RedisTemplate<String, String>();
        render.renderStringTemplate(redisTemplate);
    }

    /**
     * Enter frequency session and own it
     *
     * @param frequencyKey  the key for frequency session
     * @param duration  the duration time
     * @return session for leave usage, null if failed
     */
    public FrequencyDurationSession enterFrequencyDuration(String frequencyKey, Duration duration) {
        String frequencyValue = UUID.randomUUID().toString() + ":" + SAFE_ATOM.getAndIncrement();
        Boolean result =
                redisTemplate.execute(UNSAFE_LOCK_SCRIPT, Collections.singletonList(frequencyKey),
                        frequencyValue, String.valueOf(duration.toMillis()));
        return result ? new FrequencyDurationSession(frequencyKey, frequencyValue) : null;
    }

    /**
     * Leave frequency session
     *
     * @param session  the session for leave
     * @return false if not owned
     */
    public boolean leaveFrequencyDuration(FrequencyDurationSession session) {
        if (session == null) {
            return false;
        }

        return redisTemplate.execute(UNSAFE_UNLOCK_SCRIPT,
                Collections.singletonList(session.getFrequencyKey()), session.getFrequencyValue());
    }

}
