package com.nagarro.task.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.nagarro.task.config.AuthoritiesConstants;
import com.nagarro.task.controller.dto.AuthenticationDTO;
import com.nagarro.task.controller.response.AuthenticationResponse;
import com.nagarro.task.service.AuthenticationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;

@RestController
@CrossOrigin
@Api(tags = "Authentication")
public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping(value = "/api/authenticate")
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
			@Valid @RequestBody AuthenticationDTO authenticationRequest) throws Exception {

		logger.info("Request from user [{}] to login", authenticationRequest.getUsername());

		return ResponseEntity.ok(authenticationService.login(authenticationRequest));
	}

	@PostMapping(value = "/api/logingout")
	@PreAuthorize("hasAnyRole(\"" + AuthoritiesConstants.ROLE_ADMIN + "\") or  hasAnyRole(\""
			+ AuthoritiesConstants.ROLE_USER + "\")")
	public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization") String authorization,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		authenticationService.logout();
		return ResponseEntity.ok().build();
	}
}
