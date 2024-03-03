package org.vxwo.springboot.experience.mybatis.handlers;

import java.sql.SQLException;
import java.util.Map;
import org.apache.ibatis.type.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author vxwo-team
 */

@MappedTypes({Map.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MapJsonTypeHandler<K, V> extends BaseJsonTypeHandler<Map<K, V>> {
    @Override
    protected Map<K, V> parse(String json) throws SQLException {
        try {
            return (Map<K, V>) OBJECT_MAPPER.readValue(json, new TypeReference<Map<K, V>>() {});
        } catch (JsonProcessingException ex) {
            throw new SQLException(ex);
        }
    }
}
