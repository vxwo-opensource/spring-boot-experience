package org.vxwo.springboot.experience.web.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.vxwo.springboot.experience.web.config.RequestLoggingConfig;
import org.vxwo.springboot.experience.web.entity.RequestLoggingEntity;
import org.vxwo.springboot.experience.web.handler.RequestLoggingHandler;

/**
 * @author vxwo-team
 */

@Aspect
public class RequestLoggingAspect {

    private final int stacktraceLimitLines;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    private RequestLoggingHandler processHandler;

    public RequestLoggingAspect(RequestLoggingConfig value) {
        stacktraceLimitLines = value.getStacktraceLimitLines();
    }

    private static boolean ignoreParameterByAnnotation(MethodParameter methodParam) {
        boolean ignoreParameter = false;
        for (Annotation annotation : methodParam.getParameterAnnotations()) {
            ignoreParameter = PathVariable.class.isInstance(annotation)
                    || RequestAttribute.class.isInstance(annotation)
                    || RequestHeader.class.isInstance(annotation)
                    || SessionAttribute.class.isInstance(annotation)
                    || CookieValue.class.isInstance(annotation);;
        }
        return ignoreParameter;
    }

    private static boolean ignoreParameterByValue(Object parameterValue) {
        return parameterValue != null && (parameterValue instanceof HttpServletRequest
                || parameterValue instanceof HttpServletResponse
                || parameterValue instanceof WebRequest);
    }

    private static boolean isSimpleClass(Class<?> cls) {
        return ClassUtils.isPrimitiveOrWrapper(cls) || cls.equals(String.class);
    }

    private Map<String, Object> safeSerializeAsMap(String parameterName, Object parameterValue) {
        try {
            return processHandler.convertToMap(parameterValue);
        } catch (Exception ex) {
            return new HashMap<String, Object>(5) {
                {
                    put(parameterName, parameterValue);
                }
            };
        }
    }

    private String safeSerializeAsString(Object parameterValue) {
        try {
            return processHandler.convertToString(parameterValue);
        } catch (Exception ex) {
            return "@error: Failed on serialize as String";
        }
    }

    @Around("@within(org.springframework.stereotype.Controller)||@within(org.springframework.web.bind.annotation.RestController)")
    public Object aroundRequestMethodProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestLoggingEntity entity =
                (RequestLoggingEntity) request.getAttribute(RequestLoggingEntity.ATTRIBUTE_NAME);
        if (entity == null) {
            return joinPoint.proceed();
        }

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] methodArgs = joinPoint.getArgs();
        for (int i = 0; i < methodArgs.length; i++) {
            MethodParameter methodParam = new SynthesizingMethodParameter(method, i);
            methodParam.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());

            if (ignoreParameterByAnnotation(methodParam)) {
                continue;
            }

            Object parameterValue = methodArgs[i];
            if (ignoreParameterByValue(parameterValue)) {
                continue;
            }

            String parameterName = methodParam.getParameterName();
            RequestParam requestParam = methodParam.getParameterAnnotation(RequestParam.class);
            if (requestParam != null) {
                if (StringUtils.hasText(requestParam.value())) {
                    parameterName = requestParam.value();
                }
            }

            boolean appendToBody = false;
            RequestBody requestBody = methodParam.getParameterAnnotation(RequestBody.class);
            if (requestBody != null) {
                appendToBody = true;
            }

            boolean serializeAsMap = false;
            if (parameterValue != null) {
                if (parameterValue instanceof MultipartFile) {
                    appendToBody = true;
                    MultipartFile multipartFile = (MultipartFile) parameterValue;
                    parameterName = multipartFile.getName();
                    parameterValue = "@file=" + multipartFile.getOriginalFilename();
                }

                if (!isSimpleClass(parameterValue.getClass())) {
                    serializeAsMap = true;
                }
            }

            Map<String, Object> targetParams =
                    appendToBody ? entity.getRequestBody() : entity.getRequestParams();

            if (!serializeAsMap) {
                targetParams.put(parameterName, parameterValue);
            } else {
                targetParams.putAll(safeSerializeAsMap(parameterName, parameterValue));
            }
        }

        entity.setProcessed(true);

        Object result = joinPoint.proceed();
        if (result != null) {
            if (result instanceof byte[]) {
                entity.setResponseBody("@byte[" + ((byte[]) result).length + "]");
            } else if (result instanceof String) {
                entity.setResponseBody((String) result);
            } else {
                entity.setResponseBody(safeSerializeAsString(result));
            }
        }

        return result;
    }

    /**
     * Put field `owner` to current logging
     *
     * @param owner  The owner
     */
    public void putOwner(String owner) {
        if (request == null) {
            return;
        }

        RequestLoggingEntity entity =
                (RequestLoggingEntity) request.getAttribute(RequestLoggingEntity.ATTRIBUTE_NAME);
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
        if (request == null) {
            return;
        }

        RequestLoggingEntity entity =
                (RequestLoggingEntity) request.getAttribute(RequestLoggingEntity.ATTRIBUTE_NAME);
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
        if (request == null) {
            return;
        }

        RequestLoggingEntity entity =
                (RequestLoggingEntity) request.getAttribute(RequestLoggingEntity.ATTRIBUTE_NAME);
        if (entity == null) {
            return;
        }

        String title = exception.getMessage();
        if (title == null) {
            title = "java.lang.NullPointerException: null";
        }

        int lines = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        for (StackTraceElement element : exception.getStackTrace()) {
            if (++lines > stacktraceLimitLines) {
                break;
            }

            sb.append("\n  ");
            sb.append(element.toString());
        }

        entity.getCustomDetails().put(key, sb.toString());
    }
}
