package com.nagarro.task.config;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.google.gson.JsonObject;
import com.nagarro.task.exceptions.BadRequestParamterException;
import com.nagarro.task.exceptions.CommonNagarroException;
import com.nagarro.task.exceptions.LoginTrailException;

@ControllerAdvice
public class ExceptionHandlerResolver extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = CommonNagarroException.class)
	protected ResponseEntity<Object> handleCommonNagarroException(CommonNagarroException ex, WebRequest request) {
		return create(ex, HttpStatus.CONFLICT, request, "Contact system Admins");
	}

	@ExceptionHandler(value = BadRequestParamterException.class)
	protected ResponseEntity<Object> handleBadRequestParamterException(BadRequestParamterException ex,
			WebRequest request) {
		return create(ex, HttpStatus.BAD_REQUEST, request, "Validate Request Values");
	}

	@ExceptionHandler(value = { BadCredentialsException.class, UsernameNotFoundException.class })
	protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
		return create(ex, HttpStatus.UNAUTHORIZED, request, "Username or password is wrong");
	}

	@ExceptionHandler(value = { LoginTrailException.class, LoginTrailException.class })
	protected ResponseEntity<Object> handleLoginTrailException(LoginTrailException ex, WebRequest request) {
		return create(ex, HttpStatus.CONFLICT, request, "Logout first before login");
	}

	private ResponseEntity<Object> create(RuntimeException ex, HttpStatus httpStatus, WebRequest request,
			String message) {

		String reason = "Unexpected Error";
		if (ex != null && ex.getMessage() != null) {
			reason = ex.getMessage();
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("timestamp", LocalDateTime.now().toString());
		jsonObject.addProperty("Reason", reason);
		jsonObject.addProperty("Message", message);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(ex, jsonObject.toString(), headers, httpStatus, request);
	}
}
