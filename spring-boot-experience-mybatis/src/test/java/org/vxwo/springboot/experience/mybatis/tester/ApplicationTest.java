package org.vxwo.springboot.experience.mybatis.tester;

import java.io.StringReader;
import java.util.*;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {
    private final static String CREATE_TABLE_SCRIPT = String.join("", new ArrayList<String>() {
        {
            add("CREATE TABLE IF NOT EXISTS `user` (");
            add(" uid BIGINT GENERATED ALWAYS AS IDENTITY,");
            add(" `user` VARCHAR(50) NOT NULL,");
            add(" pwd VARCHAR(50),");
            add(" created_at TIMESTAMP,");
            add(" group_ids TEXT,");
            add(" group_keys TEXT,");
            add(" metadata TEXT,");
            add(" kv_entity TEXT,");
            add(" kv_entities TEXT,");
            add(" PRIMARY KEY (uid)");
            add(");");
        }
    });

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private void setupTable(SqlSessionFactory sqlSessionFactory) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ScriptRunner sr = new ScriptRunner(sqlSession.getConnection());
            sr.setLogWriter(null);
            sr.runScript(new StringReader(CREATE_TABLE_SCRIPT));
        }
    }

    @Test
    @Order(101)
    public void testSqlInsertOneShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUser("user");
        user.setCreatedAt(new Date());
        user.setGroupIds(new ArrayList<Integer>() {
            {
                add(1);
                add(2);
                add(3);
            }
        });
        user.setGroupKeys(new ArrayList<String>() {
            {
                add("1");
                add("2");
                add("3");
            }
        });
        user.setMetadata(new HashMap<String, Object>() {
            {
                put("name", "test");
            }
        });
        user.setKvEntity(new KvEntity("key1", "value1"));
        user.setKvEntities(new ArrayList<KvEntity>() {
            {
                add(new KvEntity("key2", "value2"));
            }
        });

        userMapper.insertUser(user);
        Assertions.assertEquals(1L, user.getUid());
    }

    @Test
    @Order(102)
    public void testSqlUpdateOneByIdShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUid(1L);
        user.setPwd("pwd");

        int value = userMapper.udpateUserById(user);
        Assertions.assertEquals(1, value);
    }

    @Test
    @Order(103)
    public void testSqlSelectByColumnShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUser("user");

        UserEntity value = userMapper.selectUserByColumn(user);
        Assertions.assertEquals(1L, value.getUid());
        Assertions.assertEquals(1, value.getGroupIds().get(0));
        Assertions.assertEquals("1", value.getGroupKeys().get(0));
        Assertions.assertEquals("test", value.getMetadata().get("name"));
        Assertions.assertEquals("value1", value.getKvEntity().getValue());
        Assertions.assertEquals("value2", value.getKvEntities().get(0).getValue());
    }

    @Test
    @Order(104)
    public void testSqlDeleteByColumnShouldSuccess() {
        UserEntity user = new UserEntity();
        user.setUser("user");

        int value = userMapper.deleteUserByColumn(user);
        Assertions.assertEquals(1, value);
    }

    @Test
    @Order(105)
    public void testSqlDeleteByColumnAgainShouldFailed() {
        UserEntity user = new UserEntity();
        user.setUser("user");

        int value = userMapper.deleteUserByColumn(user);
        Assertions.assertEquals(0, value);
    }
}
