package org.vxwo.springboot.experience.redis.tester;

import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vxwo.springboot.experience.redis.serializer.RedisPrefixWrapper;
import org.vxwo.springboot.experience.redis.serializer.RedisPrefixKeySerializer;
import org.vxwo.springboot.experience.redis.entity.FrequencyDurationSession;
import org.vxwo.springboot.experience.redis.processor.RedisFrequencyProcessor;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApplicationTest {

    private final static Duration FREQUENCY_DURATION = Duration.ofHours(1);

    private final static String FREQUENCY_CONCURRENCY_KEY =
            "frequency-concurrency:" + UUID.randomUUID().toString();
    private final static String FREQUENCY_INTERVAL_KEY =
            "frequency-interval:" + UUID.randomUUID().toString();

    @Autowired
    private RedisPrefixWrapper redisPrefixWrapper;

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
    @Order(102)
    public void testRedisPrefixKeySerializerShouldSuccess() {
        String value = UUID.randomUUID().toString();
        RedisPrefixKeySerializer redisPrefixKeySerializer = new RedisPrefixKeySerializer("prefix");
        byte[] serialized = redisPrefixKeySerializer.serialize(value);
        Assertions.assertEquals(value, redisPrefixKeySerializer.deserialize(serialized));
    }

    @Test
    @Order(201)
    @EnabledIfEnvironmentVariable(named = "EXPERIENCE_TEST_REDIS_HOST", matches = "[^\\ ]+")
    public void testFrequencyConcurrencyFirstShouldSuccess() {
        FrequencyDurationSession session = redisFrequencyProcessor
                .enterFrequencyDuration(FREQUENCY_CONCURRENCY_KEY, FREQUENCY_DURATION);
        Assertions.assertEquals(true, redisFrequencyProcessor.leaveFrequencyDuration(session));
    }

    @Test
    @Order(202)
    @EnabledIfEnvironmentVariable(named = "EXPERIENCE_TEST_REDIS_HOST", matches = "[^\\ ]+")
    public void testFrequencyConcurrencySecondShouldSuccess() {
        FrequencyDurationSession session = redisFrequencyProcessor
                .enterFrequencyDuration(FREQUENCY_CONCURRENCY_KEY, FREQUENCY_DURATION);
        Assertions.assertEquals(true, redisFrequencyProcessor.leaveFrequencyDuration(session));
    }

    @Test
    @Order(203)
    @EnabledIfEnvironmentVariable(named = "EXPERIENCE_TEST_REDIS_HOST", matches = "[^\\ ]+")
    public void testFrequencyConcurrencyBadValueShouldFailed() {
        redisFrequencyProcessor.enterFrequencyDuration(FREQUENCY_CONCURRENCY_KEY,
                FREQUENCY_DURATION);
        FrequencyDurationSession session =
                new FrequencyDurationSession(FREQUENCY_CONCURRENCY_KEY, "bad-value");
        Assertions.assertEquals(false, redisFrequencyProcessor.leaveFrequencyDuration(session));
    }

    @Test
    @Order(211)
    @EnabledIfEnvironmentVariable(named = "EXPERIENCE_TEST_REDIS_HOST", matches = "[^\\ ]+")
    public void testFrequencyIntervalFirstShouldSuccess() {
        FrequencyDurationSession session = redisFrequencyProcessor
                .enterFrequencyDuration(FREQUENCY_INTERVAL_KEY, FREQUENCY_DURATION);
        Assertions.assertEquals(true, session != null);
    }

    @Test
    @Order(212)
    @EnabledIfEnvironmentVariable(named = "EXPERIENCE_TEST_REDIS_HOST", matches = "[^\\ ]+")
    public void testFrequencyIntervalSecondShouldFailed() {
        FrequencyDurationSession session = redisFrequencyProcessor
                .enterFrequencyDuration(FREQUENCY_INTERVAL_KEY, FREQUENCY_DURATION);
        Assertions.assertEquals(true, session == null);
    }

}
