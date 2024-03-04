package org.vxwo.springboot.experience.mybatis.handlers;

import java.util.Map;
import org.apache.ibatis.type.*;
import com.fasterxml.jackson.databind.JavaType;

/**
 * @author vxwo-team
 */

@MappedTypes({Map.class})
public class MapJsonTypeHandler<K, V> extends BaseJsonTypeHandler<Map<K, V>> {
    @Override
    protected JavaType getGenericValueType() {
        return OBJECT_MAPPER.getTypeFactory().constructType(Map.class);
    }
}
