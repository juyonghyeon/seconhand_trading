package org.koreait.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    /**
     * AuthenticationException exception
     * - 인증 실패시에 발생하는 예외
     * - 예외는 상황에따라서 다양한 예외 클래스
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

    }
}
