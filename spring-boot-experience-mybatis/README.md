spring-boot-experience-mybatis
==============================================

Mybatis support

# Configuration

- [Default Settings](src/main/resources/experience/experience-mybatis.yml)
- [Example Settings](src/test/resources/application.yml)

prefix: sbexp.mybatis.general-sql

| *Key*            | *Type* | *Required* | *Default* | *Description*                       |
|------------------|--------|------------|-----------|-------------------------------------|
| reserved-prefix  | String |            |           | **Prefix** to quote reserved names  |
| reserved-stuffix | String |            |           | **Stuffix** to quote reserved names |

# GeneralSqlProvider Usage

## Annotations

### GeneralTable

This annotation can be used to tell GeneralSqlProvider the tale name.

### GeneralId

This annotation can be used to tell GeneralSqlProvider the `id` column.

### Examples

```java
...

@Data
@GeneralTable(value = "user")
public static class UserEntity {
    @GeneralId
    private Long uid;

    private String user;
    private String pwd;
    private Date createdAt;
}
...
```

## Method: insertOne

Return the INSERT statement against the target object ignore `null` fields.

### Examples

```java
public interface UserMapper {
    @InsertProvider(value=GeneralSqlProvider.class method="insertOne");
    int insertUser(UserEntity value);
}

...

UserEntity user = new UserEntity();
user.setUser("user");
user.setPwd("000000");
user.setCreatedAt(new Date());
userMapper.insertUser(user);
```

## Method: updateOneById

Return the UPDATE statement against the target object ignore `null` fields, It require the `id` column exists value.

### Examples

```java
public interface UserMapper {
    @UpdateProvider(value=GeneralSqlProvider.class method="updateOneById");
    int udpateUserById(UserEntity value);
}

...

UserEntity user = new UserEntity();
user.setUid(1L);
user.setPwd("123456");
userMapper.udpateUserById(user);
```

## Method: selectByColumn

Return the SELECT statement against the target object with not `null` fields.

### Examples

```java
public interface UserMapper {
    @SelectProvider(value=GeneralSqlProvider.class method="selectByColumn");
    int selectUserByColumn(UserEntity value);
}

...

UserEntity user = new UserEntity();
user.setUser("user");
userMapper.selectUserByColumn(user);
```

## Method: deleteByColumn

Return the DELETE statement against the target object with not `null` fields.

### Examples

```java
public interface UserMapper {
    @DeleteProvider(value=GeneralSqlProvider.class method="deleteByColumn");
    int deleteByColumn(UserEntity value);
}

...

UserEntity user = new UserEntity();
user.setUser("user");
userMapper.deleteByColumn(user);
```
