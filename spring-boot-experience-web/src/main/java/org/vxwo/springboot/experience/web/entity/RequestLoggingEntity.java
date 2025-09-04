package org.vxwo.springboot.experience.web.entity;

import java.util.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @author vxwo-team
 */

@Data
public class RequestLoggingEntity {
    public final static String ATTRIBUTE_NAME =
            "SBEXP:" + UUID.randomUUID().toString() + ":RequestLogging";

    private String application;

    private String requestHost;
    private String requestUri;
    private String requestMethod;
    private String requestQuery;

    @Setter(AccessLevel.NONE)
    private Map<String, String> requestHeaders = new HashMap<>(10);
    @Setter(AccessLevel.NONE)
    private Map<String, Object> requestParams = new HashMap<>(10);
    @Setter(AccessLevel.NONE)
    private Map<String, Object> requestBody = new HashMap<>(10);
    private String requestType;
    private int requestLength;
    private String requestBodyText;

    private String owner = "";
    private String clientIp;
    private boolean processed;
    private long timeStart;
    private long timeDuration;

    private int responseStatus;
    @Setter(AccessLevel.NONE)
    private Map<String, String> responseHeaders = new HashMap<>(10);
    private String responseType;
    private int responseLength;
    private String responseBody;

    @Setter(AccessLevel.NONE)
    private Map<String, Object> customDetails = new HashMap<>(10);
}
