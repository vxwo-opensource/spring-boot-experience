package tester;

import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vxwo.springboot.experience.web.entity.RequestLoggingEntity;
import org.vxwo.springboot.experience.web.handler.RequestLoggingHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomRequestLoggingHandler implements RequestLoggingHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToMap(Object value) throws IOException {
        return objectMapper.convertValue(value, Map.class);
    }

    @Override
    public String convertToString(Object value) throws IOException {
        return objectMapper.writeValueAsString(value);
    }

    @Override
    public void publishRequestLogging(RequestLoggingEntity entity) throws IOException {
        log.info(convertToString(entity));
    }

}
