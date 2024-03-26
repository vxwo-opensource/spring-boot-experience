package tester;

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
public class TupleTest {
    @Test
    @Order(101)
    public void testTuple2() {
        Tuple2<Integer, Long> tuple2 = Tuple2.of(1, 2L);
        Assertions.assertEquals(1, tuple2.getT1());
        Assertions.assertEquals(2L, tuple2.getT2());
    }

    @Test
    @Order(102)
    public void testTuple3() {
        Tuple3<Integer, Long, String> tuple3 = Tuple3.of(1, 2L, "3");
        Assertions.assertEquals(1, tuple3.getT1());
        Assertions.assertEquals(2L, tuple3.getT2());
        Assertions.assertEquals("3", tuple3.getT3());

    }

    @Test
    @Order(103)
    public void testTuple4() {
        Tuple4<Integer, Long, String, Double> tuple4 = Tuple4.of(1, 2L, "3", 4.0);
        Assertions.assertEquals(1, tuple4.getT1());
        Assertions.assertEquals(2L, tuple4.getT2());
        Assertions.assertEquals("3", tuple4.getT3());
        Assertions.assertEquals(4.0, tuple4.getT4());
    }

    @Test
    @Order(104)
    public void testTuple5() {
        Tuple5<Integer, Long, String, Double, Float> tuple5 = Tuple5.of(1, 2L, "3", 4.0, 5.0f);
        Assertions.assertEquals(1, tuple5.getT1());
        Assertions.assertEquals(2L, tuple5.getT2());
        Assertions.assertEquals("3", tuple5.getT3());
        Assertions.assertEquals(4.0, tuple5.getT4());
        Assertions.assertEquals(5.0f, tuple5.getT5());
    }

    @Test
    @Order(105)
    public void testTuple6() {
        Tuple6<Integer, Long, String, Double, Float, String> tuple6 =
                Tuple6.of(1, 2L, "3", 4.0, 5.0f, "6");
        Assertions.assertEquals(1, tuple6.getT1());
        Assertions.assertEquals(2L, tuple6.getT2());
        Assertions.assertEquals("3", tuple6.getT3());
        Assertions.assertEquals(4.0, tuple6.getT4());
        Assertions.assertEquals(5.0f, tuple6.getT5());
        Assertions.assertEquals("6", tuple6.getT6());
    }

    @Test
    @Order(106)
    public void testTuple7() {
        Tuple7<Integer, Long, String, Double, Float, String, Integer> tuple7 =
                Tuple7.of(1, 2L, "3", 4.0, 5.0f, "6", 7);
        Assertions.assertEquals(1, tuple7.getT1());
        Assertions.assertEquals(2L, tuple7.getT2());
        Assertions.assertEquals("3", tuple7.getT3());
        Assertions.assertEquals(4.0, tuple7.getT4());
        Assertions.assertEquals(5.0f, tuple7.getT5());
        Assertions.assertEquals("6", tuple7.getT6());
        Assertions.assertEquals(7, tuple7.getT7());
    }

    @Test
    @Order(107)
    public void testTuple8() {
        Tuple8<Integer, Long, String, Double, Float, String, Integer, Long> tuple8 =
                Tuple8.of(1, 2L, "3", 4.0, 5.0f, "6", 7, 8L);
        Assertions.assertEquals(1, tuple8.getT1());
        Assertions.assertEquals(2L, tuple8.getT2());
        Assertions.assertEquals("3", tuple8.getT3());
        Assertions.assertEquals(4.0, tuple8.getT4());
        Assertions.assertEquals(5.0f, tuple8.getT5());
        Assertions.assertEquals("6", tuple8.getT6());
        Assertions.assertEquals(7, tuple8.getT7());
        Assertions.assertEquals(8L, tuple8.getT8());
    }

}
