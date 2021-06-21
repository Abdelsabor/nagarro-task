package com.nagarro.task.config;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("timestamp", LocalDateTime.now().toString());
		jsonObject.addProperty("message", "Access denied");

		response.getWriter().write(jsonObject.toString());

	}
}
