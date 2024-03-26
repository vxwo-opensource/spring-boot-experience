package tester.handlers;

import java.util.List;
import org.vxwo.springboot.experience.mybatis.handlers.BaseJsonTypeHandler;
import tester.entity.KvEntity;
import com.fasterxml.jackson.databind.JavaType;

public class KvEntityListTypeHandler extends BaseJsonTypeHandler<List<KvEntity>> {
    @Override
    protected JavaType getGenericValueType() {
        return OBJECT_MAPPER.getTypeFactory().constructCollectionLikeType(List.class,
                KvEntity.class);
    }
}
