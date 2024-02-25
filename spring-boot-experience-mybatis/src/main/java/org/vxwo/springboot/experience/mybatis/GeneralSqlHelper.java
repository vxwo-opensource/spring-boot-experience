package org.vxwo.springboot.experience.mybatis;

import org.apache.ibatis.session.Configuration;
import org.vxwo.springboot.experience.mybatis.config.MybatisConfig;
import org.vxwo.springboot.experience.mybatis.sql.*;

/**
 * @author vxwo-team
 */

public final class GeneralSqlHelper {
    public static class ReservedRender implements SqlRender {
        private final String reservedPrefix;
        private final String reservedStuffix;

        private ReservedRender(String prefix, String stuffix) {
            this.reservedPrefix = prefix;
            this.reservedStuffix = stuffix;
        }

        @Override
        public String reserved(String reserved) {
            return reservedPrefix + reserved + reservedStuffix;
        }

        @Override
        public String property(String property) {
            return "#{" + property + "}";
        }
    }

    private static boolean camelCaseToUnderscore = false;
    private static SqlRender sqlRender = new ReservedRender("", "");

    public static void initialize(MybatisConfig config, Configuration sessionConfig) {
        camelCaseToUnderscore = sessionConfig.isMapUnderscoreToCamelCase();
        sqlRender = new ReservedRender(config.getGeneralSql().getReservedPrefix(),
                config.getGeneralSql().getReservedStuffix());
    }

    public static SqlRender getRender() {
        return sqlRender;
    }

    public static boolean isCamelCaseToUnderscore() {
        return camelCaseToUnderscore;
    }
}
