package org.vxwo.springboot.experience.web.testor;

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
            String path, String detail) throws ServletException, IOException {
        response.getWriter().write(ReturnCode.FAILED);
    }

}
