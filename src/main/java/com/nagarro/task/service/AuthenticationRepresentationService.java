package com.nagarro.task.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.task.dao.AuthenticationRepresentationDao;
import com.nagarro.task.model.AuthenticationRepresentation;

@Service
public class AuthenticationRepresentationService {

	@Autowired
	private AuthenticationRepresentationDao authenticationRepresentationDao;

	public Optional<AuthenticationRepresentation> findById(String id) {
		return authenticationRepresentationDao.findById(id);
	}

	public void save(AuthenticationRepresentation representation) {
		authenticationRepresentationDao.save(representation);
	}

	public void delete(AuthenticationRepresentation representation) {
		authenticationRepresentationDao.delete(representation);
	}

}
