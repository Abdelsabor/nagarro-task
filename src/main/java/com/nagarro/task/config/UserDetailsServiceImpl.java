package com.nagarro.task.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);

		if ("admin".equals(username)) {

			builder.password(passwordEncoder.encode("admin"));
			builder.roles("ADMIN");

		} else if ("user".equals(username)) {
			builder.password(passwordEncoder.encode("user"));
			builder.roles("USER");

		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		return builder.build();

	}

}
