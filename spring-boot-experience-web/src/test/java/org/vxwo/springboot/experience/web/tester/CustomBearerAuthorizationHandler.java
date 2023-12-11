package org.vxwo.springboot.experience.web.tester;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.vxwo.springboot.experience.web.handler.BearerAuthorizationHandler;

@Component
public class CustomBearerAuthorizationHandler implements BearerAuthorizationHandler {

    @Override
    public boolean processBearerToken(HttpServletRequest request, HttpServletResponse response,
            String tag, String matchPath, String bearerToken) throws ServletException, IOException {
        request.setAttribute(ReturnCode.LOGINED, "ok");
        return bearerToken.equals("tester");
    }

}
