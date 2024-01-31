package com.storeshop.scheduleportal.dto;

import jakarta.validation.constraints.NotBlank;

public class JwtRequestDto {

	@NotBlank(message = "Username must not null and empty.")
	private String username;

	@NotBlank(message = "Password must not null and empty.")
	private String password;

	public JwtRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JwtRequestDto(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "JwtRequest [username=" + username + ", password=" + password + "]";
	}

}
