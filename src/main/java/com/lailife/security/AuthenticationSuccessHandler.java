package com.lailife.security;

import com.lailife.web.rest.util.Message;
import com.lailife.web.rest.util.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Namdy on 2017/6/25.
 */
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().print(Message.fromResponseStatus(ResponseStatus.SUCCESS).toJson());
    }
}
