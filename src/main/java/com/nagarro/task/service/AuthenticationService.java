package com.nagarro.task.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nagarro.task.config.JwtUtils;
import com.nagarro.task.controller.dto.AuthenticationDTO;
import com.nagarro.task.controller.response.AuthenticationResponse;
import com.nagarro.task.dao.AuthenticationRepresentationDao;
import com.nagarro.task.exceptions.CommonNagarroException;
import com.nagarro.task.exceptions.LoginTrailException;
import com.nagarro.task.model.AuthenticationRepresentation;

@Service
public class AuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

	@Autowired
	private AuthenticationRepresentationDao authenticationRepresentationDao;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Value("${token.time.live}")
	private Long tokenTimeInMins;

	public AuthenticationResponse login(AuthenticationDTO authenticationRequest) {

		logger.info("User {} try to login", authenticationRequest.getUsername());

		Optional<AuthenticationRepresentation> optionalRepresentation;

		try {
			optionalRepresentation = authenticationRepresentationDao.findById(authenticationRequest.getUsername());
		} catch (Exception e) {
			logger.error("Error While Calling Redis {}", e);
			throw new CommonNagarroException("Error While Calling Redis");
		}

		if (optionalRepresentation.isPresent()) {
			AuthenticationRepresentation representation = optionalRepresentation.get();
			if (!representation.getToken().isEmpty()) {

				logger.error("User [{}] already login should logout first will throw exception",
						authenticationRequest.getUsername());

				throw new LoginTrailException("Logout first before login");
			}

		}

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authenticationRequest.getUsername());

		AuthenticationRepresentation representation = new AuthenticationRepresentation(
				authenticationRequest.getUsername(), jwt, tokenTimeInMins);

		try {
			authenticationRepresentationDao.save(representation);
		} catch (Exception e) {
			logger.error("Error While Calling Redis {}", e);
			throw new CommonNagarroException("Error While Calling Redis");
		}

		logger.info("User {} successfully login", authenticationRequest.getUsername());

		return new AuthenticationResponse(jwt);

	}

	public void logout() {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();

		Optional<AuthenticationRepresentation> optionalRepresentation;

		try {
			optionalRepresentation = authenticationRepresentationDao.findById(username);
		} catch (Exception e) {
			logger.error("Error While Calling Redis {}", e);
			throw new CommonNagarroException("Error While Calling Redis");
		}
		authenticationRepresentationDao.delete(optionalRepresentation.get());
	}

}
