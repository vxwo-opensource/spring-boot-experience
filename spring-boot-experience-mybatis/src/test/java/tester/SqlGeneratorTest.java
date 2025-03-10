package tester;

import java.util.Date;
import java.util.HashMap;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.vxwo.springboot.experience.mybatis.sql.*;
import tester.entity.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class SqlGeneratorTest {
    private static TableEntity sqlTable = TableParser.parseTable(UserEntity.class, true);
    private static BaseSqlRender sqlRender = new BaseSqlRender() {
        @Override
        public String renderReserved(String reserved) {
            return "[" + reserved + "]";
        }
    };

    @Test
    @Order(101)
    public void testSqlInsertOneShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUser("user");
        user.setCreatedAt(new Date());
        user.setMetadata(new HashMap<String, Object>() {
            {
                put("test", "test");
            }
        });

        String expected = "INSERT INTO [user] ([user], [created_at], [metadata])"
                + " VALUES (#{user}, #{createdAt}, #{metadata,typeHandler=org.vxwo.springboot.experience.mybatis.handlers.MapJsonTypeHandler})";
        Assertions.assertEquals(expected, SqlGenerator.insertOne(sqlRender, sqlTable, user));
    }

    @Test
    @Order(102)
    public void testSqlUpdateOneByIdShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUid(0L);
        user.setPwd("pwd");

        String expected = "UPDATE [user] SET [pwd]=#{pwd} WHERE [uid]=#{uid}";
        Assertions.assertEquals(expected, SqlGenerator.updateOneById(sqlRender, sqlTable, user));
    }

    @Test
    @Order(103)
    public void testSqlUpdateOneAddByIdShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUid(0L);
        user.setPwd("pwd");
        user.setCount(123L);

        String expected =
                "UPDATE [user] SET [pwd]=#{pwd}, [count]=[count]+#{count} WHERE [uid]=#{uid}";
        Assertions.assertEquals(expected, SqlGenerator.updateOneAddById(sqlRender, sqlTable, user));
    }

    @Test
    @Order(104)
    public void testSqlSelectByColumnShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUser("user");
        user.setPwd("pwd");

        String expected = "SELECT * FROM [user] WHERE [user]=#{user} AND [pwd]=#{pwd}";
        Assertions.assertEquals(expected, SqlGenerator.selectByColumn(sqlRender, sqlTable, user));
    }

    @Test
    @Order(105)
    public void testSqlDeleteByColumnShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUser("user");
        user.setPwd("pwd");

        String expected = "DELETE FROM [user] WHERE [user]=#{user} AND [pwd]=#{pwd}";
        Assertions.assertEquals(expected, SqlGenerator.deleteByColumn(sqlRender, sqlTable, user));
    }
}
