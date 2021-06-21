package com.nagarro.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		logger.info("Try to find user with name [{}]", username);

		UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);

		if ("admin".equals(username)) {

			builder.password(passwordEncoder.encode("admin"));
			builder.roles("ADMIN");

		} else if ("user".equals(username)) {
			builder.password(passwordEncoder.encode("user"));
			builder.roles("USER");

		} else {
			logger.info("User [{}] not found and will throw exception", username);
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		logger.info("User [{}] found and will build user detials", username);
		return builder.build();
	}

}