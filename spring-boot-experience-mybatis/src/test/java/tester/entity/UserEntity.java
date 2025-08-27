package tester.entity;

import java.util.*;
import org.vxwo.springboot.experience.mybatis.annotations.*;
import org.vxwo.springboot.experience.mybatis.handlers.*;
import tester.handlers.KvEntityListTypeHandler;
import lombok.Data;

@Data
@GeneralTable(value = "user")
public class UserEntity {
    @GeneralId
    private Long uid;
    private String user;
    private String pwd;
    private Date createdAt;

    @GeneralField(excluded = true)
    private Object excluded;

    @GeneralField(allowAdd = true)
    private Long count;

    @GeneralField(typeHandler = ListJsonTypeHandler.class)
    private List<Integer> groupIds;

    @GeneralField(typeHandler = ListJsonTypeHandler.class)
    private List<String> groupKeys;

    @GeneralField(typeHandler = MapJsonTypeHandler.class)
    private Map<String, Object> metadata;

    @GeneralField(typeHandler = SimpleJsonTypeHandler.class)
    private KvEntity kvEntity;

    @GeneralField(typeHandler = KvEntityListTypeHandler.class)
    private List<KvEntity> kvEntities;
}
