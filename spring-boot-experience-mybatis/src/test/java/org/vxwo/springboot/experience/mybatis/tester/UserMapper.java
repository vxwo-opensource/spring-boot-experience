package org.vxwo.springboot.experience.mybatis.tester;

import org.apache.ibatis.annotations.*;
import org.vxwo.springboot.experience.mybatis.GeneralSqlProvider;

@Mapper
public interface UserMapper {
    @InsertProvider(value = GeneralSqlProvider.class, method = "insertOne")
    @Options(useGeneratedKeys = true, keyProperty = "uid")
    int insertUser(UserEntity value);

    @UpdateProvider(value = GeneralSqlProvider.class, method = "updateOneById")
    int udpateUserById(UserEntity value);

    @SelectProvider(value = GeneralSqlProvider.class, method = "selectByColumn")
    UserEntity selectUserByColumn(UserEntity value);

    @DeleteProvider(value = GeneralSqlProvider.class, method = "deleteByColumn")
    int deleteUserByColumn(UserEntity value);
};
