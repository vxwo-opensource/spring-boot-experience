package tester;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.vxwo.springboot.experience.web.handler.SecondaryAuthorizationHandler;

@Component
public class CustomSecondaryAuthorizationHandler implements SecondaryAuthorizationHandler {
    @Override
    public boolean processSecondaryAuthorization(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(ReturnCode.LOGINED, "ok");
        return true;
    }

}
