package org.vxwo.springboot.experience.redis.tester;

import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vxwo.springboot.experience.redis.serializer.RedisPrefixKeySerializer;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class RedisPrefixKeySerializerTest {

    @Test
    @Order(101)
    public void testRedisPrefixKeySerializerShouldSuccess() {
        String value = UUID.randomUUID().toString();
        RedisPrefixKeySerializer redisPrefixKeySerializer = new RedisPrefixKeySerializer("prefix");
        byte[] serialized = redisPrefixKeySerializer.serialize(value);
        Assertions.assertEquals(value, redisPrefixKeySerializer.deserialize(serialized));
    }

}
