package org.vxwo.springboot.experience.mybatis.tester;

import java.util.Date;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.vxwo.springboot.experience.mybatis.GeneralSqlProvider;
import org.vxwo.springboot.experience.mybatis.annotations.GeneralId;
import org.vxwo.springboot.experience.mybatis.annotations.GeneralTable;
import lombok.Data;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {
    @Data
    @GeneralTable(value = "user")
    public static class UserEntity {
        @GeneralId
        private Long uid;
        private String user;
        private String pwd;
        private Date createdAt;
    }

    private GeneralSqlProvider provider = new GeneralSqlProvider();

    @Test
    @Order(101)
    public void testSqlInsertOneShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUser("user");
        user.setCreatedAt(new Date());

        String expected =
                "INSERT INTO [user] ([user], [created_at]) VALUES (#{user}, #{createdAt})";
        Assertions.assertEquals(expected, provider.insertOne(user));
    }

    @Test
    @Order(102)
    public void testSqlUpdateOneByIdShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUid(0L);
        user.setPwd("pwd");

        String expected = "UPDATE [user] SET [pwd]=#{pwd} WHERE [uid]=#{uid}";
        Assertions.assertEquals(expected, provider.updateOneById(user));
    }

    @Test
    @Order(103)
    public void testSqlSelectByColumnShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUser("user");
        user.setPwd("pwd");

        String expected = "SELECT * FROM [user] WHERE [user]=#{user} AND [pwd]=#{pwd}";
        Assertions.assertEquals(expected, provider.selectByColumn(user));
    }

    @Test
    @Order(104)
    public void testSqlDeleteByColumnShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUser("user");
        user.setPwd("pwd");

        String expected = "DELETE FROM [user] WHERE [user]=#{user} AND [pwd]=#{pwd}";
        Assertions.assertEquals(expected, provider.deleteByColumn(user));
    }
}
