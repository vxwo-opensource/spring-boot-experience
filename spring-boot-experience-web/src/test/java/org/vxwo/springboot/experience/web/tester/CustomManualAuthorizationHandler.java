package org.vxwo.springboot.experience.web.tester;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.vxwo.springboot.experience.web.handler.ManualAuthorizationHandler;

@Component
public class CustomManualAuthorizationHandler implements ManualAuthorizationHandler {

    @Override
    public boolean processManualAuthorization(HttpServletRequest request,
            HttpServletResponse response, String tag, String matchPath)
            throws ServletException, IOException {
        boolean successed = "tester".equals(request.getHeader(Constants.MANUAL_KEY));
        if (successed) {
            request.setAttribute(ReturnCode.LOGINED, "ok");
        }
        return successed;
    }

}
