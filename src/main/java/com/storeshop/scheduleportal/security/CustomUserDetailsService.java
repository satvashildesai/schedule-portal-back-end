package com.storeshop.scheduleportal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.storeshop.scheduleportal.entity.AdminCredentials;
import com.storeshop.scheduleportal.service.AdminCredentialsService;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminCredentialsService credentialsService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		AdminCredentials user = credentialsService.findByUsername(username);
		return new CustomUserDetails(user);
	}
}
