package org.vxwo.springboot.experience.util.tester;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vxwo.springboot.experience.util.ExceptionUtils;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class ExceptionTest {
    @Test
    @Order(101)
    public void testNull() {
        String data = null;
        try {
            String str = "no";
            if (!str.equals("yes")) {
                str = null;
            }

            str.equals("test");
        } catch (Exception ex) {
            data = ExceptionUtils.getStackTrace(ex);
        }

        Assertions.assertEquals(true, data.contains("testNull"));
    }

    @Test
    @Order(102)
    public void testNumbric() {
        String data = null;
        try {
            Integer.parseInt("not-numberic");
        } catch (Exception ex) {
            data = ExceptionUtils.getStackTrace(ex);
        }

        Assertions.assertEquals(true, data.contains("testNumbric"));
    }
}
