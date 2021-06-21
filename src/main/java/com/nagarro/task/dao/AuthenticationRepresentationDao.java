package com.nagarro.task.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.task.model.AuthenticationRepresentation;

@Repository
public interface AuthenticationRepresentationDao extends CrudRepository<AuthenticationRepresentation, String> {

}
