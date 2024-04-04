package org.vxwo.springboot.experience.web.processor;

import javax.servlet.http.HttpServletRequest;
import org.vxwo.springboot.experience.util.ExceptionUtils;
import org.vxwo.springboot.experience.web.config.RequestLoggingConfig;
import org.vxwo.springboot.experience.web.entity.RequestLoggingEntity;
import org.vxwo.springboot.experience.web.util.RequestUtil;

/**
 * RequestLogging helper
 *
 * @author vxwo-team
 */

public class RequestLoggingHelper {
    private final static ThreadLocal<RequestLoggingEntity> LOCAL_ENTITY = new ThreadLocal<>();

    private final int stacktraceLimitLines;

    public RequestLoggingHelper(RequestLoggingConfig value) {
        stacktraceLimitLines = value.getStacktraceLimitLines();
    }

    private RequestLoggingEntity getLoggingEntity() {
        HttpServletRequest request = RequestUtil.tryGetRequest();
        if (request != null) {
            return (RequestLoggingEntity) request.getAttribute(RequestLoggingEntity.ATTRIBUTE_NAME);
        }

        return LOCAL_ENTITY.get();
    }

    /**
     * Put field `owner` to current logging
     *
     * @param owner  The owner
     */
    public void putOwner(String owner) {
        RequestLoggingEntity entity = getLoggingEntity();
        if (entity == null) {
            return;
        }

        entity.setOwner(owner);
    }

    /**
     * Put simple object to field `customDetail` in current logging
     *
     * @param key  the key in custom detail
     * @param detail  the object to custom detail
     */
    public void putCustomDetail(String key, Object detail) {
        RequestLoggingEntity entity = getLoggingEntity();
        if (entity == null) {
            return;
        }

        entity.getCustomDetails().put(key, detail);
    }

    /**
     * Put exception to field `customDetail` in current logging
     *
     * @param key  the key in custom detail
     * @param exception  the exception to custom detail
     */
    public void putCustomDetail(String key, Exception exception) {
        RequestLoggingEntity entity = getLoggingEntity();
        if (entity == null) {
            return;
        }

        entity.getCustomDetails().put(key,
                ExceptionUtils.getStackTrace(exception, stacktraceLimitLines));
    }

    /**
     * Create thread local entity  to simulate request, MUST remove it when unused.
     *
     * @return
     */
    public RequestLoggingEntity createLocalEntity() {
        LOCAL_ENTITY.remove();

        RequestLoggingEntity entity = new RequestLoggingEntity();
        LOCAL_ENTITY.set(entity);
        return entity;
    }

    /**
     * Remove current thread local entity in simulate request
     */
    public void removeLocalEntity() {
        LOCAL_ENTITY.remove();
    }
}
