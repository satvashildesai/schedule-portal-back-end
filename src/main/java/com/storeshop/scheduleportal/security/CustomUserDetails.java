package com.storeshop.scheduleportal.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.storeshop.scheduleportal.entity.AdminCredentials;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private AdminCredentials user;

	public CustomUserDetails(AdminCredentials user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> userAuthority = new HashSet<>();

		Set<String> authority = user.getUserAuthorty();
		for (String auth : authority) {
			userAuthority.add(new SimpleGrantedAuthority(auth));
		}
		return userAuthority;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
