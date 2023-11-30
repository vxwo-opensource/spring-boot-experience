package org.vxwo.springboot.experience.web.entity;

import java.util.*;
import lombok.Data;

/**
 * @author vxwo-team
 */

@Data
public class RequestLoggingEntity {
    public final static String ATTRIBUTE_NAME = "SBEXP-RequestLogging";

    private String requestUri;
    private String requestMethod;
    private String requestQuery;
    private Map<String, String> requestHeaders = new HashMap<>(10);
    private Map<String, Object> requestParams = new HashMap<>(10);
    private Map<String, Object> requestBody = new HashMap<>(10);
    private String requestType;
    private int requestLength;

    private String owner = "";
    private String clientIp;
    private boolean processed;
    private long timeStart;
    private long timeDuration;

    private int responseStatus;
    private Map<String, String> responseHeaders = new HashMap<>(10);
    private String responseType;
    private int responseLength;
    private String responseBody;

    private Map<String, Object> customDetails = new HashMap<>(10);
}
