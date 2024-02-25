package org.vxwo.springboot.experience.mybatis;

import java.util.Objects;
import org.vxwo.springboot.experience.mybatis.sql.*;

/**
 * @author vxwo-team
 */

public final class GeneralSqlProvider {
    public <T> String insertOne(T value) {
        Objects.requireNonNull(value);
        return SqlGenerator.insertOne(GeneralSqlHelper.getRender(),
                GeneralTableCache.findTable(value.getClass()), value);
    }

    public <T> String updateOneById(T value) {
        Objects.requireNonNull(value);
        return SqlGenerator.updateOneById(GeneralSqlHelper.getRender(),
                GeneralTableCache.findTable(value.getClass()), value);
    }

    public <T> String selectByColumn(T value) {
        Objects.requireNonNull(value);
        return SqlGenerator.selectByColumn(GeneralSqlHelper.getRender(),
                GeneralTableCache.findTable(value.getClass()), value);
    }

    public <T> String deleteByColumn(T value) {
        Objects.requireNonNull(value);
        return SqlGenerator.deleteByColumn(GeneralSqlHelper.getRender(),
                GeneralTableCache.findTable(value.getClass()), value);
    }
}
