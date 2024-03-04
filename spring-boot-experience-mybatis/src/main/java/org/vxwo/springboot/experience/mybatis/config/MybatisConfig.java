package org.vxwo.springboot.experience.mybatis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

/**
 * @author vxwo-team
 */

@Data
@ConfigurationProperties(prefix = "sbexp.mybatis")
public class MybatisConfig {
    @Data
    public static class GeneralSqlConfig {
        private String reservedPrefix;
        private String reservedStuffix;
    }

    private GeneralSqlConfig generalSql;
}
