package com.storeshop.scheduleportal.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, MalformedJwtException, InsufficientAuthenticationException {

		String requestHeader = request.getHeader("Authorization");
		String username = null;
		String token = null;

		if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			token = requestHeader.substring(7);
			try {
				username = jwtHelper.getUsernameFromToken(token);

			} catch (IllegalArgumentException e) {
				logger.info("Illegal Argument while fetching the username !!");
				e.printStackTrace();
			} catch (ExpiredJwtException e) {
				logger.info("Given jwt token is expired !!");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.info("Invalid Header Value !! ");
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//			 fetch user detail from username
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			Boolean validateToken = jwtHelper.validateToken(token, userDetails);
			if (validateToken) {

//				 set the authentication
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

//				Using the buildDetails(request) method we can set remotAdrress and sessionId.
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);

			} else {
				logger.info("Validation fails !!");
			}

		}

		filterChain.doFilter(request, response);

	}

}
