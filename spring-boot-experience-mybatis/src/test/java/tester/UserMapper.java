package tester;

import org.apache.ibatis.annotations.*;
import org.vxwo.springboot.experience.mybatis.GeneralSqlProvider;
import tester.entity.UserEntity;

@Mapper
public interface UserMapper {
    @InsertProvider(value = GeneralSqlProvider.class, method = "insertOne")
    @Options(useGeneratedKeys = true, keyProperty = "uid")
    int insertUser(UserEntity value);

    @UpdateProvider(value = GeneralSqlProvider.class, method = "updateOneById")
    int udpateUserById(UserEntity value);

    @UpdateProvider(value = GeneralSqlProvider.class, method = "updateOneAddById")
    int udpateUserAddById(UserEntity value);

    @SelectProvider(value = GeneralSqlProvider.class, method = "selectByColumn")
    @ResultMap("userEntityMap")
    UserEntity selectUserByColumn(UserEntity value);

    @DeleteProvider(value = GeneralSqlProvider.class, method = "deleteByColumn")
    int deleteUserByColumn(UserEntity value);
};
