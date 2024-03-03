package org.vxwo.springboot.experience.mybatis.handlers;

import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.type.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author vxwo-team
 */

@MappedTypes({List.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ListJsonTypeHandler<T> extends BaseJsonTypeHandler<List<T>> {
    @Override
    protected List<T> parse(String json) throws SQLException {
        try {
            return (List<T>) OBJECT_MAPPER.readValue(json, new TypeReference<List<T>>() {});
        } catch (JsonProcessingException ex) {
            throw new SQLException(ex);
        }
    }
}
