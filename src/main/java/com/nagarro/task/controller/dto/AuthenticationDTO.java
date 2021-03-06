package com.nagarro.task.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class AuthenticationDTO implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	// need default constructor for JSON Parsing
	public AuthenticationDTO() {

	}

	public AuthenticationDTO(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
