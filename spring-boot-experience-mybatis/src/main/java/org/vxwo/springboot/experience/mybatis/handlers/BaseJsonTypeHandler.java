package org.vxwo.springboot.experience.mybatis.handlers;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;
import org.vxwo.springboot.experience.util.json.ObjectMapperBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author vxwo-team
 */

@SuppressWarnings("PMD")
public abstract class BaseJsonTypeHandler<T> extends BaseTypeHandler<T> {
    protected final static ObjectMapper OBJECT_MAPPER =
            ObjectMapperBuilder.builder().useDefault().build();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            ps.setString(i, OBJECT_MAPPER.writeValueAsString(parameter));
        } catch (JsonProcessingException ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return !StringUtils.hasText(json) ? null : parseValue(json);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return !StringUtils.hasText(json) ? null : parseValue(json);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return !StringUtils.hasText(json) ? null : parseValue(json);
    }

    @SuppressWarnings("unchecked")
    private T parseValue(String json) throws SQLException {
        try {
            return (T) OBJECT_MAPPER.readValue(json, getGenericValueType());
        } catch (JsonProcessingException ex) {
            throw new SQLException(ex);
        }
    }

    protected abstract JavaType getGenericValueType();
}
