package org.vxwo.springboot.experience.util.tester;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vxwo.springboot.experience.util.lang.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class ApplicationTest {


    @Test
    @Order(101)
    public void testTuple() {
        Tuple2<Integer, Long> tuple2 = Tuple2.of(1, 2L);
        Assertions.assertEquals(1, tuple2.getT1());
        Assertions.assertEquals(2L, tuple2.getT2());

        Tuple3<Integer, Long, String> tuple3 = Tuple3.of(1, 2L, "3");
        Assertions.assertEquals(1, tuple3.getT1());
        Assertions.assertEquals(2L, tuple3.getT2());
        Assertions.assertEquals("3", tuple3.getT3());

        Tuple4<Integer, Long, String, Double> tuple4 = Tuple4.of(1, 2L, "3", 4.0);
        Assertions.assertEquals(1, tuple4.getT1());
        Assertions.assertEquals(2L, tuple4.getT2());
        Assertions.assertEquals("3", tuple4.getT3());
        Assertions.assertEquals(4.0, tuple4.getT4());

        Tuple5<Integer, Long, String, Double, Float> tuple5 = Tuple5.of(1, 2L, "3", 4.0, 5.0f);
        Assertions.assertEquals(1, tuple5.getT1());
        Assertions.assertEquals(2L, tuple5.getT2());
        Assertions.assertEquals("3", tuple5.getT3());
        Assertions.assertEquals(4.0, tuple5.getT4());
        Assertions.assertEquals(5.0f, tuple5.getT5());
    }

}
