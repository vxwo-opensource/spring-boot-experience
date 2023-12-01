package org.vxwo.springboot.experience.web.tester;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.vxwo.springboot.experience.web.handler.FrequencyControlFailureHandler;

@Component
public class CustomFrequencyControlFailureHandler implements FrequencyControlFailureHandler {

    @Override
    public void handleFrequencyControlFailure(HttpServletRequest request,
            HttpServletResponse response, String method, String matchPath, String message)
            throws ServletException, IOException {
        response.getWriter().write(ReturnCode.FAILED);
    }

}
