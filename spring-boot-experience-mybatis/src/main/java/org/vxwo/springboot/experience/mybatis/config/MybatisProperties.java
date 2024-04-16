package org.vxwo.springboot.experience.mybatis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

/**
 * @author vxwo-team
 */

@Data
@ConfigurationProperties(prefix = "sbexp.mybatis")
public class MybatisProperties {
    @Data
    public static class GeneralSqlProperties {
        private String reservedPrefix;
        private String reservedStuffix;
    }

    private GeneralSqlProperties generalSql;
}
