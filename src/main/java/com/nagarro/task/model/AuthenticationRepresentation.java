package com.nagarro.task.model;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("AuthenticationRepresentation")
public class AuthenticationRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String validationId;

	private String token;

	@TimeToLive(unit = TimeUnit.MINUTES)
	private Long expirationDate;

	public AuthenticationRepresentation() {
	}

	public AuthenticationRepresentation(String validationId, String token, Long expirationDate) {
		this.validationId = validationId;
		this.token = token;
		this.expirationDate = expirationDate;
	}

	public String getValidationId() {
		return validationId;
	}

	public void setValidationId(String validationId) {
		this.validationId = validationId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Long expirationDate) {
		this.expirationDate = expirationDate;
	}

}
