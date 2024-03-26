package tester;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vxwo.springboot.experience.redis.serializer.RedisPrefixWrapper;
import org.vxwo.springboot.experience.redis.entity.FrequencyDurationSession;
import org.vxwo.springboot.experience.redis.entity.LockDurationSession;
import org.vxwo.springboot.experience.redis.processor.RedisFrequencyProcessor;
import org.vxwo.springboot.experience.redis.processor.RedisLockProcessor;

@EnabledIfEnvironmentVariable(named = "EXPERIENCE_TEST_REDIS_HOST", matches = "[^\\ ]+")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {

    private final static String LOCK_KEY = "lock:" + UUID.randomUUID().toString();
    private final static Duration LOCK_DURATION = Duration.ofHours(1);
    private final static Duration LOCK_WAITTING = Duration.ofSeconds(3);

    private final static Duration FREQUENCY_DURATION = Duration.ofHours(1);
    private final static String FREQUENCY_CONCURRENCY_KEY =
            "frequency-concurrency:" + UUID.randomUUID().toString();
    private final static String FREQUENCY_INTERVAL_KEY =
            "frequency-interval:" + UUID.randomUUID().toString();

    @Autowired
    private RedisPrefixWrapper redisPrefixWrapper;

    @Autowired
    private RedisLockProcessor redisLockProcessor;

    @Autowired
    private RedisFrequencyProcessor redisFrequencyProcessor;

    @Test
    @Order(101)
    public void testRedisPrefixShouldSuccess() {
        String value = UUID.randomUUID().toString();
        String wrapped = redisPrefixWrapper.wrap(value);
        Assertions.assertEquals(value, redisPrefixWrapper.unwrap(wrapped));
    }

    @Test
    @Order(190)
    public void testRedisLockShouldSuccess() {
        LockDurationSession session = redisLockProcessor.lock(LOCK_KEY, LOCK_DURATION);
        Assertions.assertEquals(true, redisLockProcessor.unlock(session));
    }

    @Test
    @Order(191)
    public void testRedisLockOnThreadsShouldSuccess() throws InterruptedException {
        AtomicLong counter = new AtomicLong();

        Thread first = new Thread(() -> {
            LockDurationSession session =
                    redisLockProcessor.lock(LOCK_KEY, LOCK_DURATION, LOCK_WAITTING);
            if (session != null) {
                try {
                    Thread.sleep(LOCK_WAITTING.toMillis() - 500);
                    counter.addAndGet(1);
                } catch (InterruptedException e) {
                }
                redisLockProcessor.unlock(session);
            }

        });
        Thread second = new Thread(() -> {
            LockDurationSession session =
                    redisLockProcessor.lock(LOCK_KEY, LOCK_DURATION, LOCK_WAITTING);
            if (session != null) {
                try {
                    Thread.sleep(LOCK_WAITTING.toMillis() - 500);
                    counter.addAndGet(1);
                } catch (InterruptedException e) {
                }
                redisLockProcessor.unlock(session);
            }
        });

        first.start();
        second.start();
        first.join();
        second.join();
        Assertions.assertEquals(2, counter.get());
    }

    @Test
    @Order(192)
    public void testRedisLockOnThreadsShouldFailed() throws InterruptedException {
        AtomicLong counter = new AtomicLong();

        Thread first = new Thread(() -> {
            LockDurationSession session =
                    redisLockProcessor.lock(LOCK_KEY, LOCK_DURATION, LOCK_WAITTING);
            if (session != null) {
                try {
                    Thread.sleep(LOCK_WAITTING.toMillis() + 500);
                    counter.addAndGet(1);
                } catch (InterruptedException e) {
                }
                redisLockProcessor.unlock(session);
            }

        });
        Thread second = new Thread(() -> {
            LockDurationSession session =
                    redisLockProcessor.lock(LOCK_KEY, LOCK_DURATION, LOCK_WAITTING);
            if (session != null) {
                try {
                    Thread.sleep(LOCK_WAITTING.toMillis() + 500);
                    counter.addAndGet(1);
                } catch (InterruptedException e) {
                }
                redisLockProcessor.unlock(session);
            }
        });

        first.start();
        second.start();
        first.join();
        second.join();
        Assertions.assertEquals(1, counter.get());
    }

    @Test
    @Order(201)
    public void testFrequencyConcurrencyFirstShouldSuccess() {
        FrequencyDurationSession session = redisFrequencyProcessor
                .enterFrequencyDuration(FREQUENCY_CONCURRENCY_KEY, FREQUENCY_DURATION);
        Assertions.assertEquals(true, redisFrequencyProcessor.leaveFrequencyDuration(session));
    }

    @Test
    @Order(202)
    public void testFrequencyConcurrencySecondShouldSuccess() {
        FrequencyDurationSession session = redisFrequencyProcessor
                .enterFrequencyDuration(FREQUENCY_CONCURRENCY_KEY, FREQUENCY_DURATION);
        Assertions.assertEquals(true, redisFrequencyProcessor.leaveFrequencyDuration(session));
    }

    @Test
    @Order(203)
    public void testFrequencyConcurrencyBadValueShouldFailed() {
        redisFrequencyProcessor.enterFrequencyDuration(FREQUENCY_CONCURRENCY_KEY,
                FREQUENCY_DURATION);
        FrequencyDurationSession session =
                new FrequencyDurationSession(FREQUENCY_CONCURRENCY_KEY, "bad-value");
        Assertions.assertEquals(false, redisFrequencyProcessor.leaveFrequencyDuration(session));
    }

    @Test
    @Order(211)
    public void testFrequencyIntervalFirstShouldSuccess() {
        FrequencyDurationSession session = redisFrequencyProcessor
                .enterFrequencyDuration(FREQUENCY_INTERVAL_KEY, FREQUENCY_DURATION);
        Assertions.assertEquals(true, session != null);
    }

    @Test
    @Order(212)
    public void testFrequencyIntervalSecondShouldFailed() {
        FrequencyDurationSession session = redisFrequencyProcessor
                .enterFrequencyDuration(FREQUENCY_INTERVAL_KEY, FREQUENCY_DURATION);
        Assertions.assertEquals(true, session == null);
    }

}
