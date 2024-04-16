package org.vxwo.springboot.experience.mybatis;

import org.apache.ibatis.session.Configuration;
import org.vxwo.springboot.experience.mybatis.config.MybatisProperties;
import org.vxwo.springboot.experience.mybatis.sql.*;

/**
 * @author vxwo-team
 */

public final class GeneralSqlHelper {
    public static class ReservedSqlRender extends BaseSqlRender {
        private final String reservedPrefix;
        private final String reservedStuffix;

        private ReservedSqlRender(String prefix, String stuffix) {
            this.reservedPrefix = prefix;
            this.reservedStuffix = stuffix;
        }

        @Override
        public String renderReserved(String reserved) {
            return reservedPrefix + reserved + reservedStuffix;
        }
    }

    private static Configuration mybatisConfig;
    private static BaseSqlRender mybatisSqlRender = new ReservedSqlRender("", "");

    public static void initialize(MybatisProperties config, Configuration mybatisConfig) {
        GeneralSqlHelper.mybatisConfig = mybatisConfig;
        GeneralSqlHelper.mybatisSqlRender =
                new ReservedSqlRender(config.getGeneralSql().getReservedPrefix(),
                        config.getGeneralSql().getReservedStuffix());
    }

    public static BaseSqlRender getRender() {
        return mybatisSqlRender;
    }

    public static Configuration getConfiguration() {
        return mybatisConfig;
    }
}
