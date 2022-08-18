package wystudy.jwtsecurity.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String)request.getAttribute("exception");
        ErrorCode errorCode = null;

        if (exception.equals("refresh token not available")) {
            setResponse(response, errorCode.INVALID_REFRESH_TOKEN);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(response.getStatus()+"");
        }

        else if (exception.equals("access token end")) {
            setResponse(response, errorCode.INVALID_ACCESS_TOKEN);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(response.getStatus()+"");
        }
    }

    /**
     * 한글 출력을 위해 getWriter() 사용
     */
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"message\" : \"" + errorCode.getHttpStatus()
                + "\", \"status\" : \"" + errorCode.getDetail()+ "\"}");
    }
}