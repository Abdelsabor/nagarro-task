package com.nagarro.task;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.nagarro.task.controller.dto.AuthenticationDTO;
import com.nagarro.task.controller.response.AuthenticationResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

	private final String URL = "/api/authenticate";

	@Autowired
	private TestRestTemplate testRestTemplate;

	private HttpEntity<AuthenticationDTO> request;

	@Test
	public void testBadCredentials() {

		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setUsername("admin");
		authenticationDTO.setPassword("password");

		request = new HttpEntity<>(authenticationDTO, getTrueHeaders());

		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

	}

	@Test
	public void testUserNotFound() {

		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setUsername("adminn");
		authenticationDTO.setPassword("password");

		request = new HttpEntity<>(authenticationDTO, getTrueHeaders());

		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

	}

	@Test
	public void testAuthintecationSucessLogin() {

		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setUsername("admin");
		authenticationDTO.setPassword("admin");

		request = new HttpEntity<>(authenticationDTO, getTrueHeaders());

		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());

	}

	HttpHeaders getTrueHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

}
