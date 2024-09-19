package tester;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vxwo.springboot.experience.web.matcher.PathTester;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class PathTesterTest {
    @Test
    @Order(101)
    void testMatchAll() {
        PathTester tester = new PathTester("/");
        Assertions.assertEquals(true, tester.test("/path1/path2"));
    }

    @Test
    @Order(102)
    void testMatchFull() {
        PathTester tester = new PathTester("/path1");
        Assertions.assertEquals(false, tester.test("/path1/path2"));
    }

    @Test
    @Order(103)
    void testMatchPath() {
        PathTester tester = new PathTester("/path1/");
        Assertions.assertEquals(true, tester.test("/path1"));
        Assertions.assertEquals(true, tester.test("/path1/path2"));
        Assertions.assertEquals(false, tester.test("/path111/path2"));
    }
}
