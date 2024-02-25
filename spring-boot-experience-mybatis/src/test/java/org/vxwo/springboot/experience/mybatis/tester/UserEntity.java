package org.vxwo.springboot.experience.mybatis.tester;

import java.util.Date;
import org.vxwo.springboot.experience.mybatis.annotations.GeneralId;
import org.vxwo.springboot.experience.mybatis.annotations.GeneralTable;
import lombok.Data;

@Data
@GeneralTable(value = "user")
public class UserEntity {
    @GeneralId
    private Long uid;
    private String user;
    private String pwd;
    private Date createdAt;
}
