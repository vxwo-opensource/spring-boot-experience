package org.vxwo.springboot.experience.web.tester;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.vxwo.springboot.experience.web.handler.AuthorizationFailureHandler;

@Component
public class CustomAuthorizationFailureHandler implements AuthorizationFailureHandler {

    @Override
    public void handleAuthorizationFailure(HttpServletRequest request, HttpServletResponse response,
            String tag, String path, String failureCode) throws ServletException, IOException {
        response.getWriter().write(ReturnCode.FAILED);
    }

}
