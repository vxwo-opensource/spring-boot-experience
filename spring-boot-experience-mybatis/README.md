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

### GeneralField

This annotation can be used to tell GeneralSqlProvider the column attributes.

| *Key*       | *Type*      | *Required* | *Default* | *Description*                                   |
|-------------|-------------|------------|-----------|-------------------------------------------------|
| excluded    | Boolean     |            | false     | Exclude this field                              |
| allowAdd    | Boolean     |            | false     | Allowed to be used in add operations            |
| typeHandler | TypeHandler |            |           | Specifies the TypeHandler for column conversion |

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

    @GeneralField(excluded = true)
    private Object noUsed;

    @GeneralField(allowAdd = true)
    private Long count;

    @GeneralField(typeHandler = ListJsonTypeHandler.class)
    private List<String> phones;

    private Date createdAt;
}
...
```

## Method: insertOne

Return the INSERT statement against the target object ignore `null` fields.

### Examples

```java
public interface UserMapper {
    @InsertProvider(value = GeneralSqlProvider.class, method = "insertOne")
    @Options(useGeneratedKeys = true, keyProperty = "uid")
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
    @UpdateProvider(value = GeneralSqlProvider.class, method = "updateOneById")
    int udpateUserById(UserEntity value);
}

...

UserEntity user = new UserEntity();
user.setUid(1L);
user.setPwd("123456");
userMapper.udpateUserById(user);
```

## Method: updateOneAddById

Return the UPDATE statement against the target object ignore `null` fields and use add operations for some fields, It require the `id` column exists value.

### Examples

```java
public interface UserMapper {
    @UpdateProvider(value = GeneralSqlProvider.class, method = "updateOneAddById")
    int udpateUserAddById(UserEntity value);
}

...

UserEntity user = new UserEntity();
user.setUid(1L);
user.setCount(123L);
userMapper.udpateUserAddById(user);
```

## Method: selectByColumn

Return the SELECT statement conditional on the target object with not `null` fields.

### Examples

```java
public interface UserMapper {
    @SelectProvider(value = GeneralSqlProvider.class, method = "selectByColumn")
    UserEntity selectUserByColumn(UserEntity value);
}

...

UserEntity user = new UserEntity();
user.setUser("user");
userMapper.selectUserByColumn(user);
```

## Method: deleteByColumn

Return the DELETE statement conditional on the target object with not `null` fields.

### Examples

```java
public interface UserMapper {
@DeleteProvider(value = GeneralSqlProvider.class, method = "deleteByColumn")
int deleteUserByColumn(UserEntity value);
}

...

UserEntity user = new UserEntity();
user.setUser("user");
userMapper.deleteByColumn(user);
```
