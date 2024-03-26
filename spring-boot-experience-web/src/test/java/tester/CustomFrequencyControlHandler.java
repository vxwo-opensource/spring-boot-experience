package tester;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.vxwo.springboot.experience.web.handler.FrequencyControlHandler;

@Component
public class CustomFrequencyControlHandler implements FrequencyControlHandler {
    private Map<String, Long> concurrencyMap = new HashMap<>();
    private Map<String, Long> fixedIntervalMap = new HashMap<>();

    @Override
    public boolean enterConcurrency(HttpServletRequest request, HttpServletResponse response,
            String keyPrefix, Duration duration, Map<String, Object> localContext)
            throws ServletException, IOException {
        long timestamp = System.currentTimeMillis();
        boolean result = !concurrencyMap.containsKey(keyPrefix);
        if (result) {
            localContext.put("keyPrefix", keyPrefix);
            concurrencyMap.put(keyPrefix, timestamp + duration.toMillis());
        }
        return result;
    }

    @Override
    public void leaveConcurrency(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> localContext) throws ServletException, IOException {
        concurrencyMap.remove(localContext.get("keyPrefix"));
    }

    @Override
    public boolean obtainFixedInterval(HttpServletRequest request, HttpServletResponse response,
            String keyPrefix, Duration duration) throws ServletException, IOException {
        long timestamp = System.currentTimeMillis();
        boolean result = !fixedIntervalMap.containsKey(keyPrefix);
        if (result) {
            fixedIntervalMap.put(keyPrefix, timestamp + duration.toMillis());
        }
        return result;
    }

}
