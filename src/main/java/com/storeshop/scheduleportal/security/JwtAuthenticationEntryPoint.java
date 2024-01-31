package com.storeshop.scheduleportal.security;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException, AuthenticationException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		PrintWriter writer = response.getWriter();
		writer.println("Authentication Denied!!" + authException.getMessage());

		throw new InsufficientAuthenticationException(
				"Authentication denied! please login again with valid credentials.");

//		System.err.println("..........commence() exception ==> JwtAuthenticationEntryPoint.class..........");

	}

}
