package com.storeshop.scheduleportal.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AdminCredentials {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Set<String> userAuthorty;

	public AdminCredentials() {
		super();
	}

	public AdminCredentials(Long id, String username, String password, Set<String> userAuthorty) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.userAuthorty = userAuthorty;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Set<String> getUserAuthorty() {
		return userAuthorty;
	}

	public void setUserAuthorty(Set<String> userAuthorty) {
		this.userAuthorty = userAuthorty;
	}

	@Override
	public String toString() {
		return "AdminCredentials [id=" + id + ", username=" + username + ", password=" + password + ", userAuthorty="
				+ userAuthorty + "]";
	}

}
