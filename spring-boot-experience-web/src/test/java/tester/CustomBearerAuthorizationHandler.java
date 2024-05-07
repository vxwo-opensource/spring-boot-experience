package tester;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.vxwo.springboot.experience.web.handler.BearerAuthorizationHandler;

@Component
public class CustomBearerAuthorizationHandler implements BearerAuthorizationHandler {

    @Override
    public boolean processBearerAuthorization(HttpServletRequest request,
            HttpServletResponse response, String tag, String matchPath, String bearerToken)
            throws ServletException, IOException {
        return "tester".equals(bearerToken);
    }

}
