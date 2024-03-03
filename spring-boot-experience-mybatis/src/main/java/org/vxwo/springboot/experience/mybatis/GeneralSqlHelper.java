package org.vxwo.springboot.experience.mybatis;

import org.apache.ibatis.session.Configuration;
import org.vxwo.springboot.experience.mybatis.config.MybatisConfig;
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

    private static boolean camelCaseToUnderscore = false;
    private static BaseSqlRender mybatisSqlRender = new ReservedSqlRender("", "");

    public static void initialize(MybatisConfig config, Configuration sessionConfig) {
        camelCaseToUnderscore = sessionConfig.isMapUnderscoreToCamelCase();
        mybatisSqlRender = new ReservedSqlRender(config.getGeneralSql().getReservedPrefix(),
                config.getGeneralSql().getReservedStuffix());
    }

    public static BaseSqlRender getRender() {
        return mybatisSqlRender;
    }

    public static boolean isCamelCaseToUnderscore() {
        return camelCaseToUnderscore;
    }
}
