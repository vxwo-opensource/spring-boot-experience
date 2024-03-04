package org.vxwo.springboot.experience.mybatis.handlers;

import java.util.List;
import org.apache.ibatis.type.*;
import com.fasterxml.jackson.databind.JavaType;

/**
 * @author vxwo-team
 */

@MappedTypes({List.class})
public class ListJsonTypeHandler<T> extends BaseJsonTypeHandler<List<T>> {
    @Override
    protected JavaType getGenericValueType() {
        return OBJECT_MAPPER.getTypeFactory().constructType(List.class);
    }
}
