package tester;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.vxwo.springboot.experience.web.handler.ManualAuthorizationHandler;

@Component
public class CustomManualAuthorizationHandler implements ManualAuthorizationHandler {

    @Override
    public boolean processManualAuthorization(HttpServletRequest request,
            HttpServletResponse response, String tag, String matchPath)
            throws ServletException, IOException {
        return "tester".equals(request.getHeader(Constants.MANUAL_KEY));
    }

}
